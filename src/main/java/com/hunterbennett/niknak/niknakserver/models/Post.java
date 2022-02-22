package com.hunterbennett.niknak.niknakserver.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Post extends Base {
    public static enum PostTypes { Post, Request };
    private String Title;
    private String Description;
    private String Category;
    private List<String> Colours;
    private String UserId;
    private String Date;
    private List<String> Images;
    private int Views;
    private boolean IsActiveTrade;
    private PostTypes PostType;
    private double Lat;
    private double Lng;
    private List<String> Tags;

    public Post(String id) {
        this.Id = id;
    }
}

