package com.batman.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;


public interface BaseSqlDao<T, ID> {

    Logger LOGGER = LoggerFactory.getLogger("BaseSqlDao");

    T save(T model);

    Optional<T> getById(ID id);

    void delete(T model);

    List<T> getAllByIds(Iterable<ID> ids);

    List<T> saveAll(Iterable<T> entities);

    Integer countAllByIdIn(Set<ID> ids);

    default boolean existsAllById(Set<ID> ids) {
        LOGGER.info("Entering BaseSqlDao existsAllById ...");
        boolean isExistsAll = false;
        Integer count = countAllByIdIn(ids);
        LOGGER.debug("Fetched Count:: {}",count);
        if (count != null && count.equals(ids.size())) {
            isExistsAll = true;
        }
        LOGGER.info("Exiting BaseSqlDao existsAllById ...");
        return isExistsAll;
    }
}
