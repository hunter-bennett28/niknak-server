package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;

public interface IFirestoreRepository<T> {
    public T get(T record);
    public List<T> getAll();
    public T add(T record);
    public boolean update(T record);
    public boolean delete(T record);
}
