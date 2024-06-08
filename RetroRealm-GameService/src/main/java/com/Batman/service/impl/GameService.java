package com.Batman.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.PageableRequestDto;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.entity.Game;
import com.Batman.entity.GameOwner;
import com.Batman.exception.wrapper.GameNotFoundException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.mapper.CommonMapper;
import com.Batman.projections.GameName;
import com.Batman.repository.IGameOwnerRepository;
import com.Batman.repository.IGameRepository;
import com.Batman.service.IGameService;

import lombok.RequiredArgsConstructor;

@Service("GameService")
@RequiredArgsConstructor
@Transactional
public class GameService implements IGameService{
	
	private final IGameRepository gameRepository;
	
	private final IGameOwnerRepository gameOwnerRepository;
	
	private final CommonMapper mapper;

	@Override
	public GameResponse registerGame(GameRequest gameRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		GameOwner gameOwner = gameOwnerRepository.findById(gameRequest.getGameOwnerID()).orElseThrow(()-> new GameOwnerNotFoundException("OWNER_NOT_FOUND_FOR_ID"));
		Game game = mapper.convertToEntity(gameRequest, Game.class);
		game.setGameOwner(gameOwner);
		return mapper.convertToResponse(gameRepository.save(game), GameResponse.class);
	}

	@Override
	public GameResponse searchById(Integer gameId) {
		Game game = gameRepository.findById(gameId).orElseThrow(()-> new GameNotFoundException("GAME_NOT_FOUND_FOR_ID"));
		return mapper.convertToResponse(game, GameResponse.class);
	}

	@Override
	public Page<GameResponse> getAllGames(PageableRequestDto pageableRequest) {
		PageRequest pageRequest = PageRequest.of(pageableRequest.getPageNumber(), pageableRequest.getPageSize(), pageableRequest.getAsc()?Direction.ASC:Direction.DESC, pageableRequest.getProperty());
		Page<Game> gameList = gameRepository.findAll(pageRequest);
		return gameList.map(game ->  mapper.convertToResponse(game, GameResponse.class));
	}

	@Override
	public Double getTotalCostOfGames(Set<Integer> gameIds) {
		List<Game> games = gameRepository.findAllById(gameIds);
		if(games.isEmpty()) {
			throw new GameNotFoundException("WRONG_GAME_ID_INFO");
		}
		return games.stream().mapToDouble(Game::getGamePrice).sum();
	}

	@Override
	public List<GameName> suggestAllGameNameWithPrefix(String namePrefix) {
		List<GameName> games = gameRepository.findByGameNameStartsWith(namePrefix,GameName.class);
		return games;
	}

}
