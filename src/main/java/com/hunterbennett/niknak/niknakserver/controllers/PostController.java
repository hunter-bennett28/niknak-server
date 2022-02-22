package com.hunterbennett.niknak.niknakserver.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hunterbennett.niknak.niknakserver.models.Conversation;
import com.hunterbennett.niknak.niknakserver.models.Post;
import com.hunterbennett.niknak.niknakserver.models.Report;
import com.hunterbennett.niknak.niknakserver.models.User;
import com.hunterbennett.niknak.niknakserver.repositories.ConversationRepository;
import com.hunterbennett.niknak.niknakserver.repositories.PostRepository;
import com.hunterbennett.niknak.niknakserver.repositories.ReportRepository;
import com.hunterbennett.niknak.niknakserver.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@CrossOrigin
@RestController
@RequestMapping("/posts")
public class PostController {

    // A list of common filler words to ignore in search strings
    private static final List<String> skippedKeywords = new ArrayList<>(Arrays.asList("and", "or", "of", "at"));
    private static final Logger LOG = LoggerFactory.getLogger(PostController.class);
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final ConversationRepository conversationRepo;
    private final ReportRepository reportRepo;
    
    public PostController(PostRepository postRepo, UserRepository userRepo, ConversationRepository conversationRepo,
            ReportRepository reportRepo) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.conversationRepo = conversationRepo;
        this.reportRepo = reportRepo;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        try {
            return new ResponseEntity<>(postRepo.get(new Post(id)), HttpStatus.OK);
        }
        catch (Exception e) {
            LOG.error("Controller failed to get post: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable String id) {
        try {
            return new ResponseEntity<>(postRepo.getUserPosts(id), HttpStatus.OK);
        }
        catch (Exception e) {
            LOG.error("Controller failed to get user posts: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(
            @RequestParam( required = false, defaultValue = "") String search,
            @RequestParam( required = false, defaultValue = "") String category,
            @RequestParam( required = false, defaultValue = "" ) String colour,
            @RequestParam int distance,
            @RequestParam String userId,
            @RequestParam( required = false, defaultValue = "0" ) int type) {
        try {
            System.out.println("Getting posts: " + search + " " + category + " " + colour + " " + distance + " " + userId + " " + type);
            List<Post> allPosts = postRepo.getAll();
            List<String> colours = colour.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(colour.split(",")));
            List<String> searchWords = search.isEmpty()
                ? new ArrayList<>()
                : new ArrayList<>(Arrays.asList(search.split(" ")));
            // Filter out filler keywords to make matches more accurate
            List<String> filteredSearchWords = searchWords.stream().filter(word -> {
                return !PostController.skippedKeywords.contains(word);
            }).toList();

            // Apply all other filters to list
            List<Post> filteredPosts = allPosts.stream().filter(post -> {
                // Do not return a user's own posts
                if (post.getUserId().equals(userId)) return false;

                // Match post type
                if (post.getPostType() != type) return false;

                // Only do complex queries if searching for Posts, Requests do not support extra meta data
                if (type == Post.POST_TYPE) {
                    // Check for category criteria match
                    boolean categoryMatch = category.isEmpty() || post.getCategory().equals(category);
                    if (!categoryMatch) return false;

                    // Check for colour criteria match
                    boolean colourMatch = true;
                    if (colours.size() > 0) {
                        colourMatch = false;
                        for (String col : colours) {
                            if (post.getColours().contains(col)) {
                                colourMatch = true;
                                break;
                            }
                        }
                        if (!colourMatch) return false;
                    }

                    // Check search keywords
                    boolean searchMatch = true;
                    if (filteredSearchWords.size() > 0) {
                        searchMatch = false;
                        for (String w : filteredSearchWords){
                            if (post.getTitle().toLowerCase().contains(w) || post.getDescription().toLowerCase().contains(w)
                                    || (post.getTags() != null && post.getTags().contains(w.toLowerCase()))) {
                                searchMatch = true;
                                break;
                            }
                        }
                        if (!searchMatch) return false;
                    }
                }

                // Check distance
                User user = userRepo.get(new User(userId));
                
                //If the user was banned it will be null
                if (user == null) return false;

                // Convert each lat/lng to radians
                double userLat = (user.getLat() * Math.PI) / 180;
                double userLng = (user.getLng() * Math.PI) / 180;
                double postLat = (post.getLat() * Math.PI) / 180;
                double postLng = (post.getLng() * Math.PI) / 180;

                // Haversine formula
                double dlng = postLng - userLng;
                double dlat = postLat - userLat;
                double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(userLat) * Math.cos(postLat) * Math.pow(Math.sin(dlng / 2), 2);
                double c = 2 * Math.asin(Math.sqrt(a));

                int RADIUS_OF_EARTH_IN_KM = 6371;

                double dist = c * RADIUS_OF_EARTH_IN_KM;
                if (dist > distance) return false;

                return true;
            }).toList();
               
            return new ResponseEntity<>(filteredPosts, HttpStatus.OK);
        }
        catch (Exception e) {
            LOG.error("Controller failed to get posts: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<Boolean> updatePost(@RequestBody Post post) {
        try {
            // TODO: Upload base64 images to cloud
            return new ResponseEntity<>(postRepo.update(post), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to update post: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePost(@PathVariable String id) {
        try {
            Post post = postRepo.get(new Post(id));
            
            // Check if post has already been deleted
            if (post == null)
                return new ResponseEntity<>(true, HttpStatus.OK);

            // Delete associated converations
            List<Conversation> allConvos = conversationRepo.getAll();
            for (Conversation convo : allConvos) {
                if (convo.getPostID().equals(id))
                    conversationRepo.delete(convo);
            }

            // Delete associated reports
            List<Report> allReports = reportRepo.getAll();
            for (Report report : allReports) {
                if (report.getReportedEntityID().equals(id))
                    reportRepo.delete(report);
            }

            return new ResponseEntity<>(postRepo.delete(post), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to delete post: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/view/{id}")
    public ResponseEntity<Boolean> incrementPostViews(@PathVariable String id) {
        try {
            Post post = postRepo.get(new Post(id));
            if (post == null)
                return new ResponseEntity<>(true, HttpStatus.OK);
            post.setViews(post.getViews() + 1);
            return new ResponseEntity<>(postRepo.update(post), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to increment post views: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
