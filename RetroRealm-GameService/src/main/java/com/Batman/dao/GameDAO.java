package com.Batman.dao;

import com.Batman.annotations.CacheDistribute;
import com.Batman.entity.Game;
import com.Batman.repository.IGameRepository;
import com.batman.dao.BaseSqlDao;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.Batman.constants.GameConstants.GAME;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class GameDAO implements BaseSqlDao<Game,String> {

    private final IGameRepository gameRepository;

    @Override
    @CacheEvict(value = GAME, key = "#game.gameId", condition = "#game.gameId != null")
    public Game save(Game game) {
        log.info("Entering Game Dao save ...");
        Game savedGame = gameRepository.save(game);
        log.debug("Saved ID :: {}",savedGame.getGameId());
        log.info("Leaving Game Dao save ...");
        return savedGame;
    }


    @Override
    @Cacheable(value = GAME, key = "#gameId")
    @CacheDistribute
    public Optional<Game> getById(String gameId) {
        log.info("Entering Game Dao getById ...");
        log.debug("Game Id :: {}",gameId);
        Optional<Game> game = gameRepository.findById(gameId);
        log.info("Leaving Game Dao getById ...");
        return game;
    }

    @Override
    public void delete(Game model) {
        log.info("Entering Game Dao delete ...");
        gameRepository.delete(model);
        log.info("Leaving Game Dao delete ...");
    }

    @Override
    public List<Game> getAllByIds(Iterable<String> ids) {
        log.info("Entering Game Dao getAllByIds ...");
        log.debug("Game Ids :: {}",ids);
        List<Game> games = gameRepository.findAllById(ids);
        log.debug("Fetched Games :: {}",games);
        log.info("Leaving Game Dao getAllByIds ...");
        return games;
    }

    @Override
    @CacheEvict(value = GAME, allEntries = true)
    public List<Game> saveAll(Iterable<Game> entities) {
        log.info("Entering Game Dao saveAll ...");
        List<Game> games = gameRepository.saveAll(entities);
        log.debug("Saved Games :: {}",games);
        log.info("Leaving Game Dao saveAll ...");
        return games;
    }

    @Override
    public Integer countAllByIdIn(Set<String> strings) {
        log.info("Entering Game Dao countAllByIdIn ...");
        log.debug("Game Ids are :: {}",strings);
        Integer count = gameRepository.countAllByIdIn(strings);
        log.debug("Fetched Count :: {}",count);
        log.info("Leaving Game Dao countAllByIdIn ...");
        return count;
    }
}

