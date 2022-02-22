package com.hunterbennett.niknak.niknakserver.models;

import java.util.List;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Conversation extends Base {
    @PropertyName("PostId")
    private String postID;
    @PropertyName("Messages")
    private List<Message> messages;
    @PropertyName("Users")
    private List<UserChatTracker> users;

    public Conversation(String id) {
        this.id = id;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void setUserRead(String userId) {
        for (UserChatTracker userChat : users) {
            if (userChat.getUserId().equals(userId)) {
                userChat.setLastSeen(System.currentTimeMillis());
                break;
            }
        }
    }
}
