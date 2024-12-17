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

import com.Batman.dto.PageableRequestDto;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.dto.gameowner.GameOwnerRequest;
import com.Batman.dto.gameowner.GameOwnerResponse;
import com.Batman.projections.GameName;
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
		return new ResponseEntity<>(game, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getGameById(@PathVariable final Integer id){
		return ResponseEntity.ok(gameService.searchById(id));
	}
	
	
	@PostMapping("/owner")
	public ResponseEntity<?> registerOwner(@Valid @RequestBody final GameOwnerRequest gameOwnerRequest,BindingResult bindingResult){
		GameOwnerResponse registerOwner = gameOwnerService.registerOwner(gameOwnerRequest,bindingResult);
		return new ResponseEntity<>(registerOwner,HttpStatus.CREATED);
	}
	
	@GetMapping("/owner/{id}")
	public ResponseEntity<?> getById(@PathVariable final Integer id){
		return ResponseEntity.ok(gameOwnerService.getGameOwnerById(id));
	}
	
	
	@PostMapping("/all")
	public ResponseEntity<?> fetchAllGames(@RequestBody(required = false) PageableRequestDto pageableRequest){
		return ResponseEntity.ok(gameService.getAllGames());
	}
	
	@GetMapping("/calculate/price")
	public ResponseEntity<?> getCostOfGames(@RequestParam("ids[]") final Set<Integer> gameIds) {
		return ResponseEntity.ok(gameService.getTotalCostOfGames(gameIds));
	}
	
	@GetMapping("/names")
	public List<String> getNamesByGameId(@RequestParam("ids[]") final Set<Integer> gameIds) {
		return gameService.getAllGameNameWithIds(gameIds).stream().map(GameName::getGameName).toList();
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<?> findGamesWithPrefix(@RequestParam("name") final String gamePrefix) {
		return ResponseEntity.ok(gameService.suggestAllGameNameWithPrefix(gamePrefix));
	}
	
	
//	@PutMapping("/add/discount")
//	public ResponseEntity<?> addDiscountToGames(@RequestParam final Set<Integer> gameIds,@RequestParam Double discountValue){
//		return ResponseEntity.ok(gameService.updateDiscountOfGames(gameIds,discountValue));
//	}

}
