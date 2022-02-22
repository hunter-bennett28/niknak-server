package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;

import com.hunterbennett.niknak.niknakserver.models.Ratings;

import org.springframework.stereotype.Repository;

@Repository
public class RatingsRepository implements IFirestoreRepository<Ratings> {
    private final String COLLECTION = "ratings";
    private final BaseRepository repo;

    public RatingsRepository(BaseRepository baseRepository) throws Exception {
        // this.repo = new BaseRepository(COLLECTION);
        this.repo = baseRepository;
        // this.repo.setCollection(COLLECTION);
    }

    @Override
    public Ratings add(Ratings ratings) {
        return repo.add(COLLECTION, ratings);
    }

    @Override
    public boolean delete(Ratings ratings) {
        return repo.delete(COLLECTION, ratings);
    }

    @Override
    public Ratings get(Ratings ratings) {
        return repo.get(COLLECTION, ratings, Ratings.class);
    }

    @Override
    public List<Ratings> getAll() {
        return repo.getAll(COLLECTION, Ratings.class);
    }

    @Override
    public boolean update(Ratings ratings) {
        return repo.update(COLLECTION, ratings);
    }

    public Ratings getByUserId(String userId) {
        return repo.GetRatingsByUserId(userId);
    }

    public boolean addOrUpdate(Ratings ratings) {
        return repo.addOrUpdate(COLLECTION, ratings);
    }
}
