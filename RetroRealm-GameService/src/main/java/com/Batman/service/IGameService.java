package com.Batman.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.Batman.dto.PageableRequestDto;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;

public interface IGameService {
	GameResponse registerGame(GameRequest gameRequest,BindingResult bindingResult);
	GameResponse searchById(Integer gameId);
	Page<GameResponse> getAllGames(PageableRequestDto pageableRequest);
	Double getTotalCostOfGames(Set<Integer> gameIds);

}
