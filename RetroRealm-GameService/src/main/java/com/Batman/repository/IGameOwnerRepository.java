package com.Batman.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.GameOwner;

/**
 * Repository interface for managing {@link GameOwner} entities.
 * This interface extends {@link JpaRepository} to provide basic CRUD operations
 * and adds custom methods for retrieving game owners based on specific criteria.
 *
 * <p>
 *     The Implementation will be a proxy class which will be provided by Spring Data JPA.
 * </p>
 *
 * <p>
 * Custom Methods:
 * <ul>
 *   <li>{@code findByEmail} - Retrieves a game owner by their email address.The Return Can be Wrapped in Optional, and it will be taken care by JPA</li>
 * </ul>
 * </p>
 *
 * @author SK
 */


public interface IGameOwnerRepository extends JpaRepository<GameOwner, Integer> {
	Optional<GameOwner> findByEmail(String email);

}
