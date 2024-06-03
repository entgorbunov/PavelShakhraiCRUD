package com.crud.service;

import java.util.List;

public interface Service<E, L> {
    void delete(L l);

    L create(E e);

    E update(E e);

    List<E> findAll();

    E findById(L id);
}
