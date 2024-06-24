package com.batman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batman.entity.Discount;

public interface IDiscountRepository extends JpaRepository<Discount, Integer>{

}
