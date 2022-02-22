package com.hunterbennett.niknak.niknakserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class User extends Base {
    private String FirstName;
    private String LastName;
    private String Image;
    private String Description;
    private double Rating;
    private double Lat;
    private double Lng;
    private boolean IsAdmin;

    public User(String id) {
        this.Id = id;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.IsAdmin = isAdmin;
    }
}
