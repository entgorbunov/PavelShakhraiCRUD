package com.crud.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<E, K> {
    E update(E entity);
    void save(E entity);
    void delete(K id);
    Optional<E> findById(K id);
    List<E> findAll();
}
