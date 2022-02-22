package com.hunterbennett.niknak.niknakserver.models;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class User extends Base {
    @PropertyName("FirstName")
    private String firstName;
    @PropertyName("LastName")
    private String lastName;
    @PropertyName("Image")
    private String image;
    @PropertyName("Description")
    private String description;
    @PropertyName("Rating")
    private double rating;
    @PropertyName("Lat")
    private double lat;
    @PropertyName("Lng")
    private double lng;
    @PropertyName("IsAdmin")
    private boolean isAdmin;

    public User(String id) {
        this.id = id;
    }
}
