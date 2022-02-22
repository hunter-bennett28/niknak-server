package com.hunterbennett.niknak.niknakserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserRating {
    private String UserID;
    private double Rating;
}
