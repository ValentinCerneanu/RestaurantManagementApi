package com.resturant.management.controllers;

import com.resturant.management.models.Item;
import com.resturant.management.models.Order;
import com.resturant.management.models.OrderItem;
import com.resturant.management.repositories.ItemRepository;
import com.resturant.management.repositories.OrderItemRepository;
import com.resturant.management.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    OrderController(OrderRepository orderRepository,
                    OrderItemRepository orderItemRepository,
                    ItemRepository itemRepository,
                    KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    public ResponseEntity<?> postOrder(@RequestBody Order newOrder) {
        try {
            Order order = orderRepository.save(newOrder);

            try{
                kafkaTemplate.send("new_order", order.toString());
            } catch (Exception ex){
                ex.printStackTrace();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order/{orderId}/addOrderItems/{itemId}")
    public ResponseEntity<?> postOrderItems(@PathVariable Long orderId,
                                            @PathVariable Long itemId,
                                            @RequestBody OrderItem newOrderItem) {
        try {
            Optional<Order> order = orderRepository.findById(orderId);
            if(order.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + orderId + " not found");

            newOrderItem.setOrder(order.get());

            Optional<Item> item = itemRepository.findById(itemId);

            if(item.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item with id " + itemId + " not found");

            newOrderItem.setItem(item.get());

            OrderItem orderItem = orderItemRepository.save(newOrderItem);

            try{
                kafkaTemplate.send("new_added_items_on_order", orderItem.toString());
            } catch (Exception ex){
                ex.printStackTrace();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(orderItem);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @GetMapping("/order/{orderId}/getOrderItems")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
        try {
            if(orderRepository.findById(orderId).isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + orderId + " not found");
            List<OrderItem> orderItems = orderItemRepository.findOrderItemByOrderId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(orderItems);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        Optional<Order> result = orderRepository.findById(id);
        if(result.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order found with id " + id);
    }

    @GetMapping("/order")
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @PatchMapping("/order/{orderId}/{status}")
    public ResponseEntity<?> setOrderStatus(@PathVariable Long orderId, @PathVariable String status){
        try {
            Double totalPrice = null;
            switch (status){
                case "paid":
                    List<OrderItem> orderItems = orderItemRepository.findOrderItemByOrderId(orderId);
                    totalPrice = 0.0;
                    for(OrderItem orderItem: orderItems) {
                        totalPrice += orderItem.getQuantity()*orderItem.getItem().getPrice();
                    }

                    break;
            }
            orderRepository.updateOrderStatus(orderId, status, totalPrice);
            try{
                Order order = orderRepository.findById(orderId).get();
                kafkaTemplate.send("changed_order_status", order.toString());
            } catch (Exception ex){
                ex.printStackTrace();
            }
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
