package com.hunterbennett.niknak.niknakserver.models;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A model representing a single rating of a user
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserRating {
    @PropertyName("UserId")
    private String userID;
    @PropertyName("Rating")
    private double rating;
}
