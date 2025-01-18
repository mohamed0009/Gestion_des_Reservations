package com.emsi.dao;

import java.util.List;

public interface IDAO<T> {
    boolean create(T obj);
    boolean update(T obj);
    boolean delete(T obj);
    T findById(int id);
    List<T> findAll();
}
