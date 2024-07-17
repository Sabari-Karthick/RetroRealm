package com.Batman.restcontroller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Batman.dto.PageableRequestDto;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.dto.gameowner.GameOwnerRequest;
import com.Batman.dto.gameowner.GameOwnerResponse;
import com.Batman.service.IGameOwnerService;
import com.Batman.service.IGameService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {
	private final IGameOwnerService gameOwnerService;

	private final IGameService gameService;

	@PostMapping("/release")
	public ResponseEntity<?> saveGame(@Valid @RequestBody final GameRequest gameRequest, BindingResult bindingResult) {
		GameResponse game = gameService.registerGame(gameRequest, bindingResult);
		return new ResponseEntity<GameResponse>(game, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getGameById(@PathVariable(value = "id")final Integer id){
		return ResponseEntity.ok(gameService.searchById(id));
	}
	
	
	@PostMapping("/owner")
	public ResponseEntity<?> registerOwner(@Valid @RequestBody final GameOwnerRequest gameOwnerRequest,BindingResult bindingResult){
		GameOwnerResponse registerOwner = gameOwnerService.registerOwner(gameOwnerRequest,bindingResult);
		return new ResponseEntity<GameOwnerResponse>(registerOwner,HttpStatus.CREATED);
	}
	
	@GetMapping("/owner/{id}")
	public ResponseEntity<?> getById(@PathVariable(value = "id")final Integer id){
		return ResponseEntity.ok(gameOwnerService.getGameOwnerById(id));
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllGames(@RequestBody final PageableRequestDto pageableRequest){
		return ResponseEntity.ok(gameService.getAllGames(pageableRequest));
	}
	
	@GetMapping("/calculate/price")
	public ResponseEntity<?> getCostOfGames(@RequestParam("ids[]") final Set<Integer> gameIds) {
		return ResponseEntity.ok(gameService.getTotalCostOfGames(gameIds));
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<?> findGamesWithPrefix(@RequestParam("name") final String gamePrefix) {
		return ResponseEntity.ok(gameService.suggestAllGameNameWithPrefix(gamePrefix));
	}
	
	
	@PatchMapping("/add/discount")
	public ResponseEntity<?> addDiscountToGames(@RequestParam final Set<Integer> gameIds){
		return ResponseEntity.ok(gameService.updateDiscountOfGames(gameIds));
	}

}
