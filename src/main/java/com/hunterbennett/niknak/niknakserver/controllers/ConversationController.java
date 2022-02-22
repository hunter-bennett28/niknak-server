package com.hunterbennett.niknak.niknakserver.controllers;

import java.util.ArrayList;
import java.util.List;

import com.hunterbennett.niknak.niknakserver.firebase.FirebaseAdmin;
import com.hunterbennett.niknak.niknakserver.models.Conversation;
import com.hunterbennett.niknak.niknakserver.models.Message;
import com.hunterbennett.niknak.niknakserver.models.Post;
import com.hunterbennett.niknak.niknakserver.models.User;
import com.hunterbennett.niknak.niknakserver.models.UserChatTracker;
import com.hunterbennett.niknak.niknakserver.repositories.ConversationRepository;
import com.hunterbennett.niknak.niknakserver.repositories.PostRepository;
import com.hunterbennett.niknak.niknakserver.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/conversations")
public class ConversationController {
    private static final Logger LOG = LoggerFactory.getLogger(ConversationController.class);
    private final ConversationRepository conversationRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    
    public ConversationController(ConversationRepository conversationRepo, PostRepository postRepo,
            UserRepository userRepo, FirebaseAdmin firebase) {
        this.conversationRepo = conversationRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    /**
     * Returns all conversations of a user with the given ID
     * @param userId - the ID of the user to get
     * @return A List of all conversations that the user is a part of
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable( required = true) String userId) {
        try {
            
            List<Conversation> allConversations = conversationRepo.getUserConversations(userId);
            return new ResponseEntity<>(allConversations, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to get user conversations: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a message to a conversation
     * @param conversationId - the ID of the conversation to add the messageto
     * @param message - the message to add
     * @return true if it succeeds
     */
    @PostMapping("/{conversationId}")
    public ResponseEntity<Boolean> addMessage(@PathVariable( required = true ) String conversationId,
            @RequestBody( required = true ) Message message) {
        try {
            Conversation convo = conversationRepo.get(new Conversation(conversationId));
            message.setTimestamp(System.currentTimeMillis());
            if (message.getType().equals("image")) {
                // TODO: Add Google cloud storage
            }
            convo.addMessage(message);
            return new ResponseEntity<>(conversationRepo.update(convo), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to add message to conversation: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the timestamp that a user last read a conversation
     * @param conversationId - the conversation that was read
     * @param userId - the ID of the user that read it
     * @return true if the add succeeds
     */
    @PostMapping("/read/{conversationId}/{userId}")
    public ResponseEntity<Boolean> userReadConversation(@PathVariable( required = true ) String conversationId,
            @PathVariable( required = true ) String userId) {
        Conversation convo = conversationRepo.get(new Conversation(conversationId));
        convo.setUserRead(userId);
        return new ResponseEntity<>(conversationRepo.update(convo), HttpStatus.OK);
    }

    /**
     * Creates a new conversation between two users
     * @param senderId - the ID of the person starting the conversation
     * @param postId - the ID of the person being spoken to
     * @return the Conversation object
     */
    @PostMapping("/start/{senderId}/{postId}")
    public ResponseEntity<Conversation> startConversation(@PathVariable( required = true ) String senderId,
            @PathVariable( required = true ) String postId) {
        try {
            // Get required repo data
            Post post = postRepo.get(new Post(postId));
            User sender = userRepo.get(new User(senderId));
            User receiver = userRepo.get(new User(post.getUserId()));

            // Ensure conversation doesn't already exist
            List<Conversation> conversations = conversationRepo.getUserConversations(senderId);
            for (Conversation conversation : conversations) {
                for (UserChatTracker userChat : conversation.getUsers()) {
                    if (userChat.getUserId().equals(receiver.getId()) && conversation.getPostID() == postId) {
                        return new ResponseEntity<>(conversation, HttpStatus.OK);
                    }
                }
            }

            // Create conversation
            List<UserChatTracker> users = new ArrayList<>();
            users.add(new UserChatTracker(sender.getId(), System.currentTimeMillis()));
            users.add(new UserChatTracker(receiver.getId(), 0));
            Conversation convo = new Conversation(post.getId(), new ArrayList<>(), users);
            
            return new ResponseEntity<>(convo, HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to create conversation: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
