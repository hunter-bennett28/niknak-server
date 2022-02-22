package com.hunterbennett.niknak.niknakserver.models;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserChatTracker extends Base {
    @PropertyName("UserId")
    private String userId;
    @PropertyName("LastSeen")
    private long lastSeen;

    public UserChatTracker(String id) {
        this.id = id;
    }
}
