package com.Batman.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Cart;


public interface ICartRespository extends JpaRepository<Cart, Integer> {
      Optional<Cart>  findByUserId(Integer userId);
}
