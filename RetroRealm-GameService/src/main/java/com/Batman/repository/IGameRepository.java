package com.Batman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Game;

public interface IGameRepository extends JpaRepository<Game, Integer>{
}
