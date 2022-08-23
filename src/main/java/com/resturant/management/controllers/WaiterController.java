package com.resturant.management.controllers;

import com.resturant.management.models.Waiter;
import com.resturant.management.repositories.WaiterRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class WaiterController {
    private final WaiterRepository repository;

    WaiterController(WaiterRepository repository) {
        this.repository = repository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/waiter")
    public ResponseEntity<?> postWaiter(@RequestBody Waiter newWaiter) {
        try {
            Waiter waiter = repository.save(newWaiter);
            return ResponseEntity.status(HttpStatus.CREATED).body(waiter);
        }catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/waiter/batchInsert")
    public ResponseEntity<?> postWaiter(@RequestBody List<Waiter> newWaiterList) {
        try {
            List<Waiter> waiter = repository.saveAll(newWaiterList);
            return ResponseEntity.status(HttpStatus.CREATED).body(waiter);
        }catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad value");
        }
    }

    @GetMapping("/waiter/{id}")
    public ResponseEntity<?> getWaiterById(@PathVariable Long id){
        Optional<Waiter> result = repository.findById(id);
        if(result.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No waiter found with id " + id);
    }

    @GetMapping("/waiter")
    public List<Waiter> getAllWaiters(){
        return repository.findAll();
    }
}
