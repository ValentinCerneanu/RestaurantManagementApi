package com.resturant.management.repositories;

import com.resturant.management.models.ProjectionOrderItem;
import com.resturant.management.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT o FROM order_item o WHERE o.menuOrder.id = :orderId")
    List<ProjectionOrderItem> findOrderItemByOrderId(Long orderId);

}
