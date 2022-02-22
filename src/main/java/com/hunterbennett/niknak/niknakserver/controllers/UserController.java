package com.hunterbennett.niknak.niknakserver.controllers;

import java.util.List;

import com.hunterbennett.niknak.niknakserver.models.Conversation;
import com.hunterbennett.niknak.niknakserver.models.Post;
import com.hunterbennett.niknak.niknakserver.models.Report;
import com.hunterbennett.niknak.niknakserver.models.User;
import com.hunterbennett.niknak.niknakserver.models.UserChatTracker;
import com.hunterbennett.niknak.niknakserver.repositories.ConversationRepository;
import com.hunterbennett.niknak.niknakserver.repositories.PostRepository;
import com.hunterbennett.niknak.niknakserver.repositories.ReportRepository;
import com.hunterbennett.niknak.niknakserver.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepo;
    private final ReportRepository reportRepo;
    private final PostRepository postRepo;
    private final ConversationRepository conversationRepo;
    
    public UserController(UserRepository userRepo, ReportRepository reportRepo, PostRepository postRepo,
            ConversationRepository conversationRepo) {
        this.userRepo = userRepo;
        this.reportRepo = reportRepo;
        this.postRepo = postRepo;
        this.conversationRepo = conversationRepo;
    }

    /**
     * Gets a single user
     * @param id - the ID of the user to get
     * @return a User object
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        try {
            return new ResponseEntity<>(userRepo.get(new User(id)), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to get users: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets all users
     * @return a List of all Users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return new ResponseEntity<>(userRepo.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to get users: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a user
     * @param user - the user to add
     * @return the User object added
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userRepo.add(user), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to add user: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates a user
     * @param user - the updated User object
     * @return true if it succeeds
     */
    @PutMapping
    public ResponseEntity<Boolean> updateUser(@RequestBody User user) {
        try {
            // TODO: Save base64 images to Firebase cloud
            return new ResponseEntity<>(userRepo.update(user), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to update user: " + e.getMessage());
            return new ResponseEntity<>(true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a user
     * @param id - the ID of the user to delete
     * @return true if it succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String id) {
        try {
            User user = userRepo.get(new User(id));
            if (user == null) // User has already been deleted
                return new ResponseEntity<>(true, HttpStatus.OK);

            // Delete associated reports
            List<Report> allReports = reportRepo.getAll();
            for (Report report : allReports) {
                if (report.getReportedEntityID().equals(id) || report.getUserID().equals(id))
                    reportRepo.delete(report);
            }

            // Delete associated conversations
            List<Conversation> allConvos = conversationRepo.getAll();
            for (Conversation convo : allConvos) {
                for (UserChatTracker u : convo.getUsers()) {
                    if (u.getUserId().equals(id)) {
                        conversationRepo.delete(convo);
                        break;
                    }
                }
            }

            // Delete associated posts
            List<Post> allPosts = postRepo.getAll();
            for (Post post : allPosts) {
                if (post.getUserId() == id)
                    postRepo.delete(post);
            }

            return new ResponseEntity<>(userRepo.delete(user), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to update user: " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
