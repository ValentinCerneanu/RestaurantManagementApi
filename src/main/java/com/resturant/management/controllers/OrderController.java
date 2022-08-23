package com.resturant.management.controllers;

import com.resturant.management.models.MenuOrder;
import com.resturant.management.models.ProjectionOrderItem;
import com.resturant.management.models.OrderItem;
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


    private final KafkaTemplate<String, String> kafkaTemplate;

    OrderController(OrderRepository orderRepository,
                    OrderItemRepository orderItemRepository,
                    KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    public ResponseEntity<?> postOrder(@RequestBody MenuOrder newOrder) {
        try {
            MenuOrder order = orderRepository.save(newOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order/{id}/addOrderItems")
    public ResponseEntity<?> postOrderItems(@RequestBody OrderItem newOrderItem) {
        try {
            OrderItem order = orderItemRepository.save(newOrderItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @GetMapping("/order/{orderId}/getOrderItems")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
        try {
            List<ProjectionOrderItem> orderItems = orderItemRepository.findOrderItemByOrderId(orderId);
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
        Optional<MenuOrder> result = orderRepository.findById(id);
        if(result.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No order found with id " + id);
    }

    @GetMapping("/order")
    public List<MenuOrder> getAllOrders(){
        return orderRepository.findAll();
    }

    @PatchMapping("/order/{id}/{status}")
    public ResponseEntity<?> setOrderStatus(@PathVariable Long id, @PathVariable String status){
        try {
            orderRepository.updateOrderStatus(id, status);
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
