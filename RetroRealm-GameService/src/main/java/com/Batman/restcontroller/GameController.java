package com.Batman.restcontroller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.PageableRequest;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.dto.gameowner.GameOwnerRequest;
import com.Batman.dto.gameowner.GameOwnerResponse;
import com.Batman.projections.GameName;
import com.Batman.service.IGameOwnerService;
import com.Batman.service.IGameService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing game-related operations.
 * This controller provides endpoints for creating, retrieving, and searching games.
 * It also handles operations related to game owners.
 *
 * <p>
 * Endpoints:
 * <ul>
 *   <li>{@code /api/v1/game/release} - Creates a new game.</li>
 *   <li>{@code /api/v1/game/{id}} - Retrieves a game by its ID.</li>
 *   <li>{@code /api/v1/game/owner} - Registers a new game owner.</li>
 *   <li>{@code /api/v1/game/owner/{id}} - Retrieves a game owner by their ID.</li>
 *   <li>{@code /api/v1/game/all} - Retrieves all games with pagination.</li>
 *   <li>{@code /api/v1/game/calculate/price} - Calculates the total cost of a set of games.</li>
 *   <li>{@code /api/v1/game/names} - Retrieves the names of games by their IDs.</li>
 *   <li>{@code /api/v1/game/search} - Suggests games based on a name prefix.</li>
 * </ul>
 * </p>
 *
 * @author SK
 */

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

	private final IGameOwnerService gameOwnerService;

	private final IGameService gameService;

	/**
	 * Endpoint to create a new game.
	 *
	 * @param gameRequest   The request body containing game details.
	 * @param bindingResult The result of the validation.
	 * @return ResponseEntity with status CREATED(201) containing the created game details.
	 */

	@PostMapping("/release")
	public ResponseEntity<?> saveGame(@Valid @RequestBody final GameRequest gameRequest, BindingResult bindingResult) {
		GameResponse game = gameService.registerGame(gameRequest, bindingResult);
		return new ResponseEntity<>(game, HttpStatus.CREATED);
	}

	/**
	 * Endpoint to retrieve a game by its ID.
	 *
	 * @param id The ID of the game to retrieve.
	 * @return ResponseEntity with status OK(200) containing the game details.
	 */

	@GetMapping("/{id}")
	public ResponseEntity<?> getGameById(@PathVariable final String id) {
		return ResponseEntity.ok(gameService.searchById(id));
	}


	/**
	 * Endpoint to register a new game owner.
	 *
	 * @param gameOwnerRequest The request body containing game owner details.
	 * @param bindingResult    The result of the validation.
	 * @return ResponseEntity with status CREATED(201) containing the registered game owner details.
	 */

	@PostMapping("/owner")
	public ResponseEntity<?> registerOwner(@Valid @RequestBody final GameOwnerRequest gameOwnerRequest,
			BindingResult bindingResult) {
		GameOwnerResponse registerOwner = gameOwnerService.registerOwner(gameOwnerRequest, bindingResult);
		return new ResponseEntity<>(registerOwner, HttpStatus.CREATED);
	}

	/**
	 * Endpoint to retrieve a game owner by their ID.
	 *
	 * @param id The ID of the game owner to retrieve.
	 * @return ResponseEntity with status OK(200) containing the game owner details.
	 */

	@GetMapping("/owner/{id}")
	public ResponseEntity<?> getById(@PathVariable final Integer id) {
		return ResponseEntity.ok(gameOwnerService.getGameOwnerById(id));
	}

	/**
	 * Endpoint to retrieve all games with pagination.
	 *
	 * @param pageableRequest The request body containing pagination details.
	 * @return ResponseEntity with status OK(200) containing the paginated game details.
	 */

	@PostMapping("/all")
	public ResponseEntity<?> fetchAllGames(@RequestBody(required = false) PageableRequest pageableRequest) {
		return ResponseEntity.ok(gameService.getAllGamesAsPages(pageableRequest));
	}

	/**
	 * Endpoint to calculate the total cost of a set of games.
	 * This Endpoint is used mostly for service to service communication.
	 *
	 * @param gameIds The set of game IDs to calculate the total cost for.
	 * @return ResponseEntity with status OK(200) containing the total cost.
	 */

	@GetMapping("/calculate/price")
	public ResponseEntity<?> getCostOfGames(@RequestParam("ids[]") final Set<String> gameIds) {
		return ResponseEntity.ok(gameService.getTotalCostOfGames(gameIds));
	}

	/**
	 * Endpoint to retrieve the names of games by their IDs.
	 * This Endpoint may be removed in the future.
	 *
	 * @param gameIds The set of game IDs to retrieve names for.
	 * @return List of game names.
	 */

	@Deprecated
	@GetMapping("/names")
	public List<String> getNamesByGameId(@RequestParam("ids[]") final Set<String> gameIds) {
		throw new UnsupportedOperationException("DEPRECATED_SERVICE");
	}

	/**
	 * Endpoint to suggest games based on a name prefix.
	 * This Endpoint may be altered in the future to support autocomplete for many fields of the game.
	 *
	 * @param gameName The prefix of the game name to search for.
	 * @return ResponseEntity with status OK(200) containing the suggested game names.
	 */

	@Deprecated
	@GetMapping("/search")
	public ResponseEntity<?> suggestGame(@RequestParam("name") final String gameName) {
		throw new UnsupportedOperationException("DEPRECATED_SERVICE");
	}

	@PostMapping("/validate/ids")
	public ResponseEntity<?> validateGameIds(@RequestBody final Set<String> gameIds){
		return ResponseEntity.ok(gameService.validateGameIds(gameIds));
	}

}
