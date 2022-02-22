package com.hunterbennett.niknak.niknakserver.models;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A model represented a message sent between users
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Message extends Base {
    @PropertyName("Content")
    private String content;
    @PropertyName("FromId")
    private String fromId;
    @PropertyName("ToId")
    private String toId;
    @PropertyName("Type")
    private String type;
    @PropertyName("Timestamp")
    private long timestamp;

    public Message(String id) {
        this.id = id;
    }
}
