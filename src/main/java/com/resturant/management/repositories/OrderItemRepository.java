package com.resturant.management.repositories;

import com.resturant.management.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT o FROM order_item o WHERE o.order.id = :orderId")
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE order_item u set u.status = :status where u.id = :id")
    void updateOrderItemStatus(@Param("id") Long id, @Param("status") String status);
}

