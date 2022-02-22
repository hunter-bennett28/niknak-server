package com.hunterbennett.niknak.niknakserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserChatTracker extends Base {
    private String UserID;
    private long LastSeen;

    public UserChatTracker(String id) {
        this.Id = id;
    }
}
