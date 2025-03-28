package com.Batman.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import com.Batman.dto.PageableRequestDto;
import com.Batman.dto.events.DiscountPlacedEvent;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.projections.GameName;

public interface IGameService {
	GameResponse registerGame(GameRequest gameRequest,BindingResult bindingResult);
	GameResponse searchById(Integer gameId);
	Page<GameResponse> getAllGamesAsPages(PageableRequestDto pageableRequest);
	List<GameResponse> getAllGames();
	Double getTotalCostOfGames(Set<Integer> gameIds);
    List<GameName> suggestAllGameNameWithPrefix(String gameNameQuery);
    List<GameName> getAllGameNameWithIds(Set<Integer> gameIds);
    List<GameResponse> updateDiscountOfGames(DiscountPlacedEvent discountPlacedEvent);
}
