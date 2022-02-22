package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;

/**
 * An interface for common Firestore repository functionality
 */
public interface IFirestoreRepository<T> {
    /**
     * Gets a single record of a repository
     * @param record - the record with a populated ID to get
     * @return a single record
     */
    public T get(T record);

    /**
     * Gets all records of a repository
     * @return a List of all records
     */
    public List<T> getAll();

    /**
     * Adds a new record to a repository
     * @param record - the record to add
     * @return the record Added
     */
    public T add(T record);

    /**
     * Updates a record in a repository
     * @param record - the updated record
     * @return true if it succeeded
     */
    public boolean update(T record);

    /**
     * Deletes a record from a repository
     * @param record - the record to delete
     * @return true if it succeeded
     */
    public boolean delete(T record);
}
