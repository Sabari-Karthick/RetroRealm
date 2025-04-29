package com.Batman.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Game;
import com.Batman.projections.View;


/**
 * Repository interface for managing {@link Game} entities.
 * This interface extends {@link JpaRepository} to provide basic CRUD operations
 * and adds custom methods for retrieving games based on specific criteria.
 *
 * <p>
 * Custom Methods:
 * <ul>
 *   <li>{@code findByGameNameStartsWith} - Retrieves games whose names start with a given prefix.</li>
 *   <li>{@code findByGameIdIn} - Retrieves games whose IDs are in a given set.</li>
 * </ul>
 * </p>
 *
 * @author SK
 */
public interface IGameRepository extends JpaRepository<Game, String>{
	<T extends View> List<T> findByGameNameStartsWith(String namePrefix,Class<T> clazz);
	<T extends View> List<T> findByGameIdIn(Set<String> gameIds,Class<T> clazz);
}
