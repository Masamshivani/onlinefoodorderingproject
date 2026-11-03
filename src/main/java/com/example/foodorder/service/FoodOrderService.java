package com.example.foodorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.foodorder.exception.InvalidOrderUpdateException;
import com.example.foodorder.exception.OrderNotFoundException;
import com.example.foodorder.model.FoodOrder;

@Service
public class FoodOrderService {

    private final List<FoodOrder> orders = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<FoodOrder> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public FoodOrder getOrderById(Long id) {
        return findOrder(id);
    }

    public FoodOrder addOrder(FoodOrder order) {
        order.setOrderId(nextId.getAndIncrement());
        orders.add(order);
        return order;
    }

    public FoodOrder updateOrder(Long id, FoodOrder updatedOrder) {
        FoodOrder existingOrder = findOrder(id);
        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setFoodItem(updatedOrder.getFoodItem());
        existingOrder.setQuantity(updatedOrder.getQuantity());
        existingOrder.setPrice(updatedOrder.getPrice());
        return existingOrder;
    }

    public FoodOrder partiallyUpdateOrder(Long id, Map<String, Object> updates) {
        FoodOrder order = findOrder(id);

        if (updates == null || updates.isEmpty()) {
            throw new InvalidOrderUpdateException("Provide at least one field to update.");
        }

        for (String field : updates.keySet()) {
            if (!List.of("customerName", "foodItem", "quantity", "price").contains(field)) {
                throw new InvalidOrderUpdateException("Field cannot be updated: " + field);
            }
        }

        if (updates.containsKey("customerName")) {
            Object value = updates.get("customerName");
            if (!(value instanceof String) || ((String) value).isBlank()) {
                throw new InvalidOrderUpdateException("Customer name is required and must be text.");
            }
            order.setCustomerName(((String) value).trim());
        }

        if (updates.containsKey("foodItem")) {
            Object value = updates.get("foodItem");
            if (!(value instanceof String) || ((String) value).isBlank()) {
                throw new InvalidOrderUpdateException("Food item is required and must be text.");
            }
            order.setFoodItem(((String) value).trim());
        }

        if (updates.containsKey("quantity")) {
            Object value = updates.get("quantity");
            if (!(value instanceof Number) || ((Number) value).doubleValue() < 1
                    || ((Number) value).doubleValue() % 1 != 0) {
                throw new InvalidOrderUpdateException("Quantity must be a whole number of at least 1.");
            }
            order.setQuantity(((Number) value).intValue());
        }

        if (updates.containsKey("price")) {
            Object value = updates.get("price");
            if (!(value instanceof Number)) {
                throw new InvalidOrderUpdateException("Price must be a number greater than 0.");
            }
            BigDecimal price = new BigDecimal(value.toString());
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidOrderUpdateException("Price must be greater than 0.");
            }
            order.setPrice(price);
        }

        return order;
    }

    public void deleteOrder(Long id) {
        FoodOrder order = findOrder(id);
        orders.remove(order);
    }

    private FoodOrder findOrder(Long id) {
        if (id == null || id < 1) {
            throw new OrderNotFoundException("Order ID must be a positive number.");
        }

        for (FoodOrder order : orders) {
            if (order.getOrderId().equals(id)) {
                return order;
            }
        }

        throw new OrderNotFoundException("No food order exists with ID " + id + ".");
    }
}
