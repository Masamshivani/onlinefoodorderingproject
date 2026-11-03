package com.example.foodorder.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodorder.model.FoodOrder;
import com.example.foodorder.service.FoodOrderService;

@RestController
@RequestMapping("/api/orders")
public class FoodOrderController {

    private final FoodOrderService foodOrderService;

    public FoodOrderController(FoodOrderService foodOrderService) {
        this.foodOrderService = foodOrderService;
    }

    @GetMapping
    public ResponseEntity<List<FoodOrder>> getAllOrders() {
        return ResponseEntity.ok(foodOrderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(foodOrderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<FoodOrder> addOrder(@Valid @RequestBody FoodOrder order) {
        FoodOrder savedOrder = foodOrderService.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodOrder> updateOrder(@PathVariable Long id,
                                                  @Valid @RequestBody FoodOrder order) {
        return ResponseEntity.ok(foodOrderService.updateOrder(id, order));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FoodOrder> partiallyUpdateOrder(@PathVariable Long id,
                                                           @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(foodOrderService.partiallyUpdateOrder(id, updates));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        foodOrderService.deleteOrder(id);
        return ResponseEntity.ok(Map.of("message", "Order deleted successfully."));
    }
}
