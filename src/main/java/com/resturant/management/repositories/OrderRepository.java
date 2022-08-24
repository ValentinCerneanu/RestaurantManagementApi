package com.resturant.management.repositories;

import com.resturant.management.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE order_t u set u.status = :status, u.totalPrice = :totalPrice where u.id = :id")
    void updateOrderStatus(@Param("id") Long id, @Param("status") String status, @Param("totalPrice") Double totalPrice);
}
