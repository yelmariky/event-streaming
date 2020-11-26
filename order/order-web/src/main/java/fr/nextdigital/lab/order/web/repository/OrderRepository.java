package fr.nextdigital.lab.order.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import fr.nextdigital.lab.order.web.domain.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByAccountId(@Param("accountId") Long accountId);
}
