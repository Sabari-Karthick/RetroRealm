package com.Batman.service.impl;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.Batman.events.GameEvent;
import com.batman.constants.CrudAction;
import com.batman.dao.BaseSqlDao;
import com.batman.elastic.IRetroESBaseRepository;
import com.batman.exception.wrapper.InputFieldException;
import com.batman.helpers.BaseHelper;
import com.batman.helpers.CommonMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.Batman.constants.KafkaConstants;
import com.Batman.dto.PageableRequest;
import com.Batman.dto.events.DiscountPlacedEvent;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.entity.Game;
import com.Batman.entity.GameOwner;
import com.Batman.exception.wrapper.GameNotFoundException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.TooManyRequestException;
import com.Batman.projections.GameName;
import com.Batman.repository.IGameOwnerRepository;
import com.Batman.service.IGameService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("gameService")
@RequiredArgsConstructor
public class GameService implements IGameService {

    private final IGameOwnerRepository gameOwnerRepository;

    private final IRetroESBaseRepository<Game> gameEsRepository;

    private final BaseSqlDao<Game,String> gameDao;

    private final CommonMapper mapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private static final String SERVICE_NAME = "game-service";

    @Override
    public GameResponse registerGame(GameRequest gameRequest, BindingResult bindingResult) {
        log.info("Entering Game Service Register Game ...");
        if (bindingResult.hasErrors()) {
            String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                    .map(FieldError::getDefaultMessage)
                    .orElse("Unknown error in Gaming Request");
            log.error("Binding Error in Game Service {}", errorMessage);
            throw new InputFieldException(errorMessage);
        }
        GameOwner gameOwner = gameOwnerRepository.findById(gameRequest.getGameOwnerID())
                .orElseThrow(() -> new GameOwnerNotFoundException("OWNER_NOT_FOUND_FOR_ID"));
        Game game = mapper.convertToEntity(gameRequest, Game.class);
        game.setGameOwner(gameOwner);
        game.setGameDiscount(0.0);
        Game savedGame = gameDao.save(game);
        log.info("Game Saved With Id :: {}", savedGame.getGameId());
        GameResponse gameResponse = mapper.convertToResponse(savedGame, GameResponse.class);
        GameEvent gameEvent = new GameEvent(this, savedGame, CrudAction.CREATE);
        applicationEventPublisher.publishEvent(gameEvent);
        log.info("Leaving Game Service Register Game ...");
        return gameResponse;
    }

    @Override
    public GameResponse searchById(String gameId) {
        log.info("Entering get Game By Id for Id :: {}", gameId);
        Game game = gameDao.getById(gameId)
                .orElseThrow(() -> new GameNotFoundException("GAME_NOT_FOUND_FOR_ID"));
        GameResponse gameResponse = mapper.convertToResponse(game, GameResponse.class);
        log.info("Leaving Get Game By Id ...");
        return gameResponse;
    }

    @Override
    public Page<GameResponse> getAllGamesAsPages(PageableRequest pageableRequest) {
        log.info("Entering getAllGames by page... ");
        Page<Game> gamePages =  gameEsRepository.getAllByPage(BaseHelper.getQueryConditionsFromFilters(pageableRequest.getFilters()),BaseHelper.buildSort(pageableRequest.getSortField(),pageableRequest.isAsc()), pageableRequest.getPageNumber(), pageableRequest.getPageSize(), Game.class);
        Page<GameResponse> response = gamePages.map(game -> mapper.convertToResponse(game, GameResponse.class));
        log.debug("Game Counts for Game Service getAllGamesAsPages :: {}", response.getSize());
        log.info("Leaving getAllGames by page... ");
        return response;
    }

    @Override
    public Double getTotalCostOfGames(Set<String> gameIds) {
        log.info("Entering GameService getTotalCostOfGames ...");
        log.debug("Game Ids :: {}",gameIds);
        List<Game> games = gameDao.getAllByIds(gameIds);
        if (CollectionUtils.isEmpty(games)) {
            log.error("Game Ids are not found in the system for Ids {}",gameIds);
            throw new GameNotFoundException("GAME_NOT_FOUND_FOR_ID");
        }
        double totalCost = games.stream().mapToDouble(Game::getDiscountedGamePrice).sum();
        log.debug("Total Cost Calculated :: {}",totalCost);
        log.info("Leaving GameService getTotalCostOfGames ...");
        return totalCost;
    }

    @Override
    @Deprecated
    public List<GameName> suggestAllGameNameWithPrefix(String gameNameQuery) {
//        return gameRepository.findByGameNameStartsWith(gameNameQuery, GameName.class);
          return null;
    }

    @Override
    @KafkaListener(topics = KafkaConstants.TOPIC, groupId = KafkaConstants.GROUP_ID)
    public List<GameResponse> updateDiscountOfGames(DiscountPlacedEvent discountPlacedEvent) {
        Set<String> gameIds = discountPlacedEvent.getGameIds();
        Double discountValue = discountPlacedEvent.getDiscountValue();
        log.info("Update Request For {} with value {}", gameIds, discountValue);
        List<Game> games = gameDao.getAllByIds(gameIds);
        if (games.isEmpty()) {
            log.error("No Games Found with the provided Ids...");
            throw new GameNotFoundException("No Games Found");
        }
        games.forEach(game -> game.setGameDiscount(discountValue));
        games.forEach(game -> log.info("Updated Discounts of game {} with ID :: {} is {}%", game.getGameName(), game.getGameId(), game.getGameDiscount()));
        games = gameDao.saveAll(games);
        log.info("Leaving Update Game Discount Request......");
        return games.stream().map(game -> mapper.convertToResponse(game, GameResponse.class)).toList();
    }

    @Override
    public boolean validateGameIds(Set<String> gameIds) {
        log.info("Entering GameService validateGameIds ...");
        log.debug("Game Ids are:: {}",gameIds);
        boolean isValid = gameDao.existsAllById(gameIds);
        log.debug("Is Valid :: {}",isValid);
        log.info("Leaving GameService validateGameIds ...");
        return isValid;

    }

    public Double gameServiceFallBackMethod(Throwable ex) {
        log.error(ex.getMessage());
        log.info("Entering Game Service Fall Back Method ...");
        throw new TooManyRequestException("REQUEST_OVERLOADED");
    }

    @Override
    @Deprecated
    public List<GameName> getAllGameNameWithIds(Set<String> gameIds) {
//        return gameRepository.findByGameIdIn(gameIds, GameName.class);
        return null;
    }

}
