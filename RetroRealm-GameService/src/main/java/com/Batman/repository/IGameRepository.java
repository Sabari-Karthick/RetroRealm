package com.Batman.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Game;
import com.Batman.projections.View;

public interface IGameRepository extends JpaRepository<Game, String>{
	<T extends View> List<T> findByGameNameStartsWith(String namePrefix,Class<T> clazz);
	<T extends View> List<T> findByGameIdIn(Set<String> gameIds,Class<T> clazz);
}
