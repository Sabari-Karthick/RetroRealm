package com.batman.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.batman.entity.Discount;
import java.util.List;

public interface IDiscountRepository extends JpaRepository<Discount, Integer> {
	
	
	@Query(value = "Select d from Discount d join d.gameIds g where g IN :gameIds")
	List<Discount> findDiscountsOfGames(Set<String> gameIds);

	@Query( value = "Select d from Discount d join d.gameIds g where g IN :gameIds AND d.isExpired = :isExpired")
	List<Discount> findActiveDiscountsOfGames(Set<Integer> gameIds, Boolean isExpired);
}
