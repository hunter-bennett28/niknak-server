package com.hunterbennett.niknak.niknakserver.controllers;

import com.hunterbennett.niknak.niknakserver.models.Ratings;
import com.hunterbennett.niknak.niknakserver.models.User;
import com.hunterbennett.niknak.niknakserver.models.UserRating;
import com.hunterbennett.niknak.niknakserver.repositories.RatingsRepository;
import com.hunterbennett.niknak.niknakserver.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/ratings")
public class RatingsController {
    private static final Logger LOG = LoggerFactory.getLogger(RatingsController.class);
    private final UserRepository userRepo;
    private final RatingsRepository ratingsRepo;
    
    public RatingsController(UserRepository userRepo, RatingsRepository ratingsRepo) {
        this.userRepo = userRepo;
        this.ratingsRepo = ratingsRepo;
    }

    /**
     * Gets ratings for a given user
     * @param id - the ID of the user
     * @return a Ratings object containing info about all user ratings
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ratings> getRatings(@PathVariable String id) {
        try {
            return new ResponseEntity<>(ratingsRepo.getByUserId(id), HttpStatus.OK);
        }
        catch (Exception e) {
            LOG.error("Controller failed to get user ratings: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Eithers adds or updates a rating based on its existence and updates the average rating
     * @param id - the ID of the user doing the rating
     * @param userRating - the rating
     * @return true if it succeeds
     */
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> addOrUpdateRatings(@PathVariable String id, @RequestBody UserRating userRating) {
        try {
            // Load the user and rating for the user
            Ratings ratings = ratingsRepo.getByUserId(id);
            User user = userRepo.get(new User(id));

            // Add the rating if its new, otherwise update the current
            UserRating currentRating = ratings.getUserRatings().stream().filter(rating -> {
                return rating.getUserID().equals(userRating.getUserID());
            }).findFirst().orElse(null);
            if (currentRating != null) {
                if (currentRating.getRating() == userRating.getRating()) { // Client resubmitted the same information
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }

                currentRating.setRating(userRating.getRating());
            } else {
                ratings.addUserRating(userRating);
            }

            // Calculate the average rating
            double overallRating = 0.0;
            for (UserRating ur : ratings.getUserRatings())
                overallRating += ur.getRating();
            user.setRating(overallRating / ratings.getUserRatings().size());

            userRepo.update(user);
            return new ResponseEntity<>(ratingsRepo.addOrUpdate(ratings), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to add or update user ratings: " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
