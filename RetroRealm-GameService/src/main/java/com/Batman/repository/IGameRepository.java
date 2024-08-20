package com.Batman.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Game;
import com.Batman.projections.View;

public interface IGameRepository extends JpaRepository<Game, Integer>{
	<T extends View> List<T> findByGameNameStartsWith(String namePrefix,Class<T> clazz);
	<T extends View> List<T> findByGameIDIn(Set<Integer> gameIds,Class<T> clazz);
}
