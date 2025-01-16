package com.Batman.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.GameOwner;

public interface IGameOwnerRepository extends JpaRepository<GameOwner, Integer> {
	Optional<GameOwner> findByEmail(String email);

}
