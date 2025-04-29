package com.Batman.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.Batman.dto.PageableRequest;
import com.Batman.dto.events.DiscountPlacedEvent;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.projections.GameName;

/**
 * This interface provides methods for managing game-related operations.
 * It includes functionalities such as registering a new game, searching for a game by ID,
 * retrieving all games, calculating the total cost of games, and suggesting games based on a name prefix.
 *
 * <p>
 * Methods:
 * <ul>
 *   <li>{@code registerGame} - Registers a new game.</li>
 *   <li>{@code searchById} - Searches for a game by its ID.</li>
 *   <li>{@code getAllGamesAsPages} - Retrieves all games with pagination.</li>
 *   <li>{@code getAllGames} - Retrieves all games.</li>
 *   <li>{@code getTotalCostOfGames} - Calculates the total cost of a set of games.</li>
 *   <li>{@code suggestAllGameNameWithPrefix} - Suggests games based on a name prefix.</li>
 *   <li>{@code getAllGameNameWithIds} - Retrieves the names of games by their IDs.</li>
 *   <li>{@code updateDiscountOfGames} - Updates the discount of games.</li>
 * </ul>
 * </p>
 *

 *
 * @author SK
 */
public interface IGameService {
	/**
	 * Registers a new game.
	 * All Pre-conditions are checked before saving the game.
	 *
	 * <p>
	 * Pre-conditions:
	 * <ul>
	 *   <li>The game request must be valid.</li>
	 *   <li>The game owner must exist.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Post-conditions:
	 * <ul>
	 *   <li>A new game is registered in the system.</li>
	 *   <li>A GameEvent is published to notify other services about the new game.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Error-conditions:
	 * <ul>
	 *   <li>If the game request is invalid, an InputFieldException is thrown.</li>
	 *   <li>If the game owner does not exist, a GameOwnerNotFoundException is thrown.Currently</li>
	 * </ul>
	 * <ul>
	 *   <li>**Note** Currently the Event is a Crud Event to Trigger the ES index. In Future It maybe moved to aspect </li>
	 *   <li>**Note** There is an event needs to published to Kafka As well which will be handled in Future  </li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Side-effects:
	 * <ul>
	 *   <li>A new game is saved in the database.</li>
	 *   <li>A GameEvent is published.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Business-rules:
	 * <ul>
	 *   <li>The game owner must be registered in the system.</li>
	 *   <li>The game name must be unique.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Performance-considerations:
	 * <ul>
	 *   <li>The method is annotated with @CacheEvict to clear the cache after a new game is registered.</li>
	 *   <li>The method is annotated with @CacheDistribute to distribute the cache across multiple instances.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Concurrency-considerations:
	 * <ul>
	 *   <li>The method is transactional to ensure data consistency.</li>

	 *
	 * @param gameRequest   The request body containing game details.
	 * @param bindingResult The result of the validation.
	 * @return GameResponse containing the details of the registered game.
	 */

	GameResponse registerGame(GameRequest gameRequest,BindingResult bindingResult);

	/**
	 * Searches for a game by its ID.
	 *
	 * <p>
	 * Pre-conditions:
	 * <ul>
	 *   <li>The game ID must be valid.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Post-conditions:
	 * <ul>
	 *   <li>The game with the specified ID is retrieved.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Error-conditions:
	 * <ul>
	 *   <li>If the game does not exist, a GameNotFoundException is thrown.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Side-effects:
	 * <ul>
	 *   <li>None.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Business-rules:
	 * <ul>
	 *   <li>The game must exist in the system.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Performance-considerations:
	 * <ul>
	 *   <li>The method is annotated with @Cacheable to cache the game details.</li>
	 *   <li>The method is annotated with @CacheDistribute to distribute the cache across multiple instances.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Concurrency-considerations:
	 * <ul>
	 *   <li>The method is read-only and does not modify any data.</li>
	 * </ul>
	 * </p>
	 *
	 * @param gameId The ID of the game to search for.
	 * @return GameResponse containing the details of the game.
	 */

	GameResponse searchById(String gameId);

	/**
	 * Retrieves all games with pagination.
	 *
	 * <p>
	 * Pre-conditions:
	 * <ul>
	 *   <li>The pageable request must be valid.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Post-conditions:
	 * <ul>
	 *   <li>A page of games is retrieved.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Error-conditions:
	 * <ul>
	 *   <li>None.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Side-effects:
	 * <ul>
	 *   <li>None.</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * Business-rules:
	 * <ul>
	 *   <li>The games must exist in the system.</li>
	 * </ul>
	 * </p>
	 *
	 * Concurrency-considerations:
	 * <ul>
	 *   <li>The method is read-only and does not modify any data.</li>
	 * </ul>
	 * </p>
	 *
	 * @param pageableRequest The request body containing pagination details.
	 * @return Page of GameResponse containing the details of the games.
	 */
	Page<GameResponse> getAllGamesAsPages(PageableRequest pageableRequest);

    /**
     * Calculates the total cost of a set of games.
     *
     * <p>
     * Pre-conditions:
     * <ul>
     *   <li>The game IDs must be valid.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Post-conditions:
     * <ul>
     *   <li>The total cost of the games is calculated.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Error-conditions:
     * <ul>
     *   <li>If any of the game IDs are invalid, a GameNotFoundException is thrown.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Side-effects:
     * <ul>
     *   <li>None.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Business-rules:
     * <ul>
     *   <li>The games must exist in the system.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Concurrency-considerations:
     * <ul>
     *   <li>The method is read-only and does not modify any data.</li>
     * </ul>
     * </p>
     *
     * @param gameIds The IDs of the games to calculate the total cost for.
     * @return The total cost of the games.
     */

	Double getTotalCostOfGames(Set<String> gameIds);
    List<GameName> suggestAllGameNameWithPrefix(String gameNameQuery);
    List<GameName> getAllGameNameWithIds(Set<String> gameIds);
    List<GameResponse> updateDiscountOfGames(DiscountPlacedEvent discountPlacedEvent);
}
