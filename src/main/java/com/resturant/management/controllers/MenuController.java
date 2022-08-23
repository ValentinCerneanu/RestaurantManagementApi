package com.resturant.management.controllers;

import com.resturant.management.models.MenuItem;
import com.resturant.management.repositories.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MenuController {

    private final MenuRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    MenuController(MenuRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/menu")
    public ResponseEntity<?> postMenuItem(@RequestBody MenuItem newMenuItem) {
        try {
            MenuItem menuItem = repository.save(newMenuItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/menu/batchInsert")
    public ResponseEntity<?> postMenuItems(@RequestBody List<MenuItem> newMenuItems) {
        try {
            List<MenuItem> menuItem = new ArrayList<>(repository.saveAll(newMenuItems));
            return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @GetMapping("/menu/{id}")
    public ResponseEntity<?> getMenuById(@PathVariable Long id){
//        try{
//            kafkaTemplate.send("Hello-Kafka", "new request on get menu/id");
//            kafkaTemplate.flush();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
        Optional<MenuItem> result = repository.findById(id);
        if(result.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No menu item found with id " + id);
    }

    @GetMapping("/menu")
    public List<MenuItem> getAllMenuItems(){
        return repository.findAll();
    }
}
