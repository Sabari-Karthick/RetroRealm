package com.Batman.repository;

import com.Batman.entity.GameDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDiscountRepository extends JpaRepository<GameDiscount, Long> {
   GameDiscount findByGameId(String gameId);
}
