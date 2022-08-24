package com.resturant.management.controllers;

import com.resturant.management.models.Item;
import com.resturant.management.repositories.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {

    private final ItemRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    ItemController(ItemRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/item")
    public ResponseEntity<?> postItem(@RequestBody Item newItem) {
        try {
            Item item = repository.save(newItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/item/batchInsert")
    public ResponseEntity<?> postItems(@RequestBody List<Item> newItems) {
        try {
            List<Item> item = new ArrayList<>(repository.saveAll(newItems));
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        }catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id){
//        try{
//            kafkaTemplate.send("Hello-Kafka", "new request on get item/id");
//            kafkaTemplate.flush();
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
        Optional<Item> result = repository.findById(id);
        if(result.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No item found with id " + id);
    }

    @GetMapping("/item")
    public List<Item> getAllItems(){
        return repository.findAll();
    }
}
