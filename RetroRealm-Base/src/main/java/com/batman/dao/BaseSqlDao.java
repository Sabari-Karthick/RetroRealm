package com.batman.dao;

import java.util.List;
import java.util.Optional;

public interface BaseSqlDao<T,ID> {
    T save(T model);
    Optional<T> getById(ID id);
    void delete(T model);
    List<T> getAllByIds(Iterable<ID> ids);
    List<T> saveAll(Iterable<T> entities);
}
