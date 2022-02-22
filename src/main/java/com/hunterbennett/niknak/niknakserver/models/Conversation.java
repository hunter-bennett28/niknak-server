package com.hunterbennett.niknak.niknakserver.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class Conversation extends Base {
    private String PostID;
    private List<Message> Messages;
    private List<UserChatTracker> Users;

    public Conversation(String id) {
        this.Id = id;
    }

    public void addMessage(Message message) {
        this.Messages.add(message);
    }

    public void setUserRead(String userId) {
        for (UserChatTracker userChat : Users) {
            if (userChat.getUserID().equals(userId)) {
                userChat.setLastSeen(System.currentTimeMillis());
                break;
            }
        }
    }
}
