package com.resturant.management.repositories;

import com.resturant.management.models.MenuOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface OrderRepository extends JpaRepository<MenuOrder, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE menu_order u set u.status = :status where u.id = :id")
    void updateOrderStatus(@Param("id") Long id, @Param("status") String status);
}
