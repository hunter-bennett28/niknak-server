package com.hunterbennett.niknak.niknakserver.models;

import java.util.List;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A model representing a post made by a user
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Post extends Base {
    public static final int POST_TYPE = 0;
    public static final int REQUEST_TYPE = 1;
    @PropertyName("Title")
    private String title;
    @PropertyName("Description")
    private String description;
    @PropertyName("Category")
    private String category;
    @PropertyName("Colours")
    private List<String> colours;
    @PropertyName("UserId")
    private String userId;
    @PropertyName("Date")
    private String date;
    @PropertyName("Images")
    private List<String> images;
    @PropertyName("Views")
    private int views;
    @PropertyName("IsActiveTrade")
    private boolean isActiveTrade;
    @PropertyName("PostType")
    private int postType;
    @PropertyName("Lat")
    private double lat;
    @PropertyName("Lng")
    private double lng;
    @PropertyName("Tags")
    private List<String> tags;

    public Post(String id) {
        this.id = id;
    }

    public boolean getIsActiveTrade() {
        return this.isActiveTrade;
    }

    public void setIsActiveTrade(boolean isActiveTrade) {
        this.isActiveTrade = isActiveTrade;
    }
}

