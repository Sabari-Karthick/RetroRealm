package com.Batman.service.impl;

import static com.Batman.constants.GameConstants.GAME_PAGE_RESPONSE;
import static com.Batman.constants.GameConstants.GAME_RESPONSE;

import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.annotations.CacheDistribute;
import com.Batman.constants.KafkaConstants;
import com.Batman.dto.PageableRequestDto;
import com.Batman.dto.events.DiscountPlacedEvent;
import com.Batman.dto.game.GameRequest;
import com.Batman.dto.game.GameResponse;
import com.Batman.entity.Game;
import com.Batman.entity.GameOwner;
import com.Batman.exception.wrapper.GameNotFoundException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.TooManyRequestException;
import com.Batman.helper.CommonMapper;
import com.Batman.projections.GameName;
import com.Batman.repository.IGameOwnerRepository;
import com.Batman.repository.IGameRepository;
import com.Batman.service.IGameService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("GameService")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameService implements IGameService {

	private final IGameRepository gameRepository;

	private final IGameOwnerRepository gameOwnerRepository;

	private final CommonMapper mapper;

	private static final String SERVICE_NAME = "game-service";

	@Override
	@CacheEvict(value = GAME_PAGE_RESPONSE, allEntries = true)
	@CacheDistribute
	public GameResponse registerGame(GameRequest gameRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		GameOwner gameOwner = gameOwnerRepository.findById(gameRequest.getGameOwnerID())
				.orElseThrow(() -> new GameOwnerNotFoundException("OWNER_NOT_FOUND_FOR_ID"));
		Game game = mapper.convertToEntity(gameRequest, Game.class);
		game.setGameOwner(gameOwner);
		game.setGameDiscount(0.0);
		return mapper.convertToResponse(gameRepository.save(game), GameResponse.class);
	}

	@Override
	@Cacheable(value = GAME_RESPONSE, key = "#gameId")
	@CacheDistribute
	public GameResponse searchById(Integer gameId) {
		log.info("Entering get Game By Id for Id :: {}", gameId);
		Game game = gameRepository.findById(gameId)
				.orElseThrow(() -> new GameNotFoundException("GAME_NOT_FOUND_FOR_ID"));
		GameResponse gameResponse = mapper.convertToResponse(game, GameResponse.class);
		log.info("Leaving Get Game By Id ...");
		return gameResponse;
	}

	@Override
	@Cacheable(value = GAME_PAGE_RESPONSE,key = "T(com.Batman.constants.GameConstants).GAME_PAGE_RESPONSE")
	@CacheDistribute
	public Page<GameResponse> getAllGames(PageableRequestDto pageableRequest) {
		log.info("Entering getAllGames... ");
		PageRequest pageRequest = PageRequest.of(pageableRequest.getPageNumber(), pageableRequest.getPageSize(),
				pageableRequest.getAsc() ? Direction.ASC : Direction.DESC, pageableRequest.getProperty());
		Page<Game> gamePages = gameRepository.findAll(pageRequest);
		Page<GameResponse> response = gamePages.map(game -> mapper.convertToResponse(game, GameResponse.class));
		log.debug("Game Counts :: {}",response.getSize());
		log.info("Leaving getAllGames... ");
		return response;
	}
	

	@Override
	@RateLimiter(name = SERVICE_NAME, fallbackMethod = "gameServiceFallBackMethod")
	@CircuitBreaker(name = SERVICE_NAME)
	public Double getTotalCostOfGames(Set<Integer> gameIds) {
		log.info("Entering getTotalCostOfGames ...");
		List<Game> games = gameRepository.findAllById(gameIds);
		if (games.isEmpty()) {
			return 0.0;
		}
		double totalCost = games.stream().mapToDouble(Game::getDiscountedGamePrice).sum();
		log.info("Leaving getTotalCostOfGames ...");
		return totalCost;
	}

	@Override
	public List<GameName> suggestAllGameNameWithPrefix(String namePrefix) {
		log.info("Fetching Game By Prefix ...");
		List<GameName> games = gameRepository.findByGameNameStartsWith(namePrefix, GameName.class);
		log.info("Fetched Game By Prefix ...");
		return games;
	}

	@Override
	@CircuitBreaker(name = SERVICE_NAME)
	@KafkaListener(topics = KafkaConstants.TOPIC, groupId = KafkaConstants.GROUP_ID)
    @Caching(evict = {@CacheEvict(value = GAME_PAGE_RESPONSE, allEntries = true),	@CacheEvict(value = GAME_RESPONSE, allEntries = true)})
	public List<GameResponse> updateDiscountOfGames(DiscountPlacedEvent discountPlacedEvent) {
		Set<Integer> gameIds = discountPlacedEvent.getGameIds();
		Double discountValue = discountPlacedEvent.getDiscountValue();
		log.info("Update Request For {} with value {}", gameIds, discountValue);
		List<Game> games = gameRepository.findAllById(gameIds);
		if (games.isEmpty()) {
			log.error("No Games Found with the provided Ids...");
			throw new GameNotFoundException("No Games Found");
		}
		games.forEach(game -> game.setGameDiscount(discountValue));
	    games.forEach(game -> 	log.info("Updated Discounts of game {} with ID :: {} is {}%",game.getGameName(),game.getGameID(),game.getGameDiscount()));
		games = gameRepository.saveAll(games);
		log.info("Leaving Update Game Discount Request......");
		return games.stream().map(game -> mapper.convertToResponse(game, GameResponse.class)).toList();
	}

	public Double gameServiceFallBackMethod(Throwable ex) {
		log.error(ex.getMessage());
		log.info("Entering Game Service Fall Back Method ...");
		throw new TooManyRequestException("REQUEST_OVERLOADED");
	}

	@Override
	public List<GameName> getAllGameNameWithIds(Set<Integer> gameIds) {
		return gameRepository.findByGameIDIn(gameIds, GameName.class);
	}

}
