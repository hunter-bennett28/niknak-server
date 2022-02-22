package com.hunterbennett.niknak.niknakserver.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Ratings extends Base {
    private List<UserRating> UserRatings;
    private String UserID;

    public Ratings(String id) {
        this.Id = id;
    }
}
