package com.batman.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batman.entity.Discount;


public interface IDiscountRepository extends JpaRepository<Discount, Integer>{
      Optional<Discount> findByGameIds(Set<Integer> gameIds);
}
