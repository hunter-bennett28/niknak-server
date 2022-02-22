package com.hunterbennett.niknak.niknakserver.models;

import java.util.List;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Ratings extends Base {
    @PropertyName("UserRatings")
    private List<UserRating> userRatings;
    @PropertyName("UserID")
    private String userID;

    public Ratings(String id) {
        this.id = id;
    }

    public void addUserRating(UserRating rating) {
        this.userRatings.add(rating);
    }
}
