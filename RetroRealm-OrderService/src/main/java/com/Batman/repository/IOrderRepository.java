package com.Batman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
     List<Order> findAllByUserId(Integer userId);
}
