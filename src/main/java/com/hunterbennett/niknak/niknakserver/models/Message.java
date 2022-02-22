package com.hunterbennett.niknak.niknakserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Message extends Base {
    private String Content;
    private String FromId;
    private String ToId;
    private String Type;
    private long Timestamp;

    public Message(String id) {
        this.Id = id;
    }
}
