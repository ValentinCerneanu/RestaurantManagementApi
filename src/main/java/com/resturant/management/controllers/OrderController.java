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
//        try{
//            kafkaTemplate.send("Hello-Kafka", "new request on get order/id");
//            kafkaTemplate.flush();
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
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

    @PatchMapping("/order/{id}/{status}")
    public ResponseEntity<?> setOrderStatus(@PathVariable Long id, @PathVariable String status){
        try {
            //TODO switch by status -> on paid calculate totalPrice
            orderRepository.updateOrderStatus(id, status);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
