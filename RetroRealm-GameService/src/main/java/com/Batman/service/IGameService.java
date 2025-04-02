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

public interface IGameService {
	GameResponse registerGame(GameRequest gameRequest,BindingResult bindingResult);
	GameResponse searchById(String gameId);
	Page<GameResponse> getAllGamesAsPages(PageableRequest pageableRequest);
	List<GameResponse> getAllGames();
	Double getTotalCostOfGames(Set<String> gameIds);
    List<GameName> suggestAllGameNameWithPrefix(String gameNameQuery);
    List<GameName> getAllGameNameWithIds(Set<String> gameIds);
    List<GameResponse> updateDiscountOfGames(DiscountPlacedEvent discountPlacedEvent);
}
