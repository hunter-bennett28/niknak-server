package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.hunterbennett.niknak.niknakserver.firebase.FirebaseAdmin;
import com.hunterbennett.niknak.niknakserver.models.Base;
import com.hunterbennett.niknak.niknakserver.models.Conversation;
import com.hunterbennett.niknak.niknakserver.models.Post;
import com.hunterbennett.niknak.niknakserver.models.Ratings;
import com.hunterbennett.niknak.niknakserver.models.UserChatTracker;
import com.hunterbennett.niknak.niknakserver.models.UserRating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BaseRepository {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRepository.class);
    private final FirebaseAdmin firebase;

    public BaseRepository(FirebaseAdmin firebase) throws Exception {
        this.firebase = firebase;
    }

    public <T extends Base> boolean addOrUpdate(String collection, T record) {
        return record.getId() != null
            ? add(collection, record).getId() != null
            : update(collection, record);
    }

    public <T extends Base> T add(String collection, T record) {
        try {
            CollectionReference colRef = firebase.getDb().collection(collection);
            DocumentReference docRef = colRef.add(record).get();
            record.setId(docRef.getId());
            return record;
        } catch (Exception e) {
            LOG.error("Failed to add record to database: " + e.getMessage());
            return null;
        }
    }

    public <T extends Base> boolean update(String collection, T record) {
        try {
            DocumentReference docRef = firebase.getDb().collection(collection).document(record.getId());
            docRef.set(record).get();
            return true;
        } catch (Exception e) {
            LOG.error("Failed to update record in database: " + e.getMessage());
            return false;
        }
    }

    public <T extends Base> boolean delete(String collection, T record) {
        try {
            DocumentReference docRef = firebase.getDb().collection(collection).document(record.getId());
            docRef.delete().get();
            return true;
        } catch (Exception e) {
            LOG.error("Failed to delete record in database: " + e.getMessage());
            return false;
        }
    }

    public <T extends Base> T get(String collection, T record, Class<T> classType) {
        try {
            System.out.println("Collection: " + collection + " Getting id: " + record.getId());
            DocumentSnapshot documentSnapshot = firebase.getDb().collection(collection)
                .document(record.getId()).get().get();
            if (documentSnapshot.exists()) {
                T result = documentSnapshot.toObject(classType);
                result.setId(record.getId());
                System.out.println("Returning record: " + result.getClass());
                return result;
            }
            System.out.println("Returning null record");
            return null;
        } catch (Exception e) {
            LOG.error("Failed to get record from database: " + e.getMessage());
            return null;
        }
    }

    public <T extends Base> List<T> getAll(String collection, Class<T> classType) {
        try {
            Query query = firebase.getDb().collection(collection);
            QuerySnapshot querySnapshot = query.get().get();
            List<T> recordList = new ArrayList<>();

            for (DocumentSnapshot docSnapshot : querySnapshot.getDocuments()) {
                if (docSnapshot.exists()) {
                    T document = docSnapshot.toObject(classType);
                    document.setId(docSnapshot.getId());
                    recordList.add(document);
                }
            }
            System.out.println("Getting from collection: " + collection);
            System.out.println("Getting all of type: " + classType);
            return recordList;
        } catch (Exception e) {
            LOG.error("Failed to get records from database: " + e.getMessage());
            return null;
        }
    }

    // Custom Queries
    public List<Conversation> getUserConversations(String userId) {
        List<Conversation> conversations = new ArrayList<>();
        try {
            Query query = firebase.getDb().collection("conversations");
            QuerySnapshot querySnapshot = query.get().get();
            for (DocumentSnapshot documentSnapshot : querySnapshot) {
                Conversation conversation = documentSnapshot.toObject(Conversation.class);
                conversation.setId(documentSnapshot.getId());
                for (UserChatTracker userChat : conversation.getUsers()) {
                    if (userChat.getUserID() == userId) {
                        conversations.add(conversation);
                        break;
                    }
                }
            }

            return conversations.size() > 0 ? conversations : null;
        } catch (Exception e) {
            LOG.error("Failed to get user conversations from database: " + e.getMessage());
            return null;
        }
    }

    public List<Post> getUserPosts(String userId) {
        List<Post> posts = new ArrayList<>();
        try {
            Query query = firebase.getDb().collection("posts").whereEqualTo("UserId", userId);
                QuerySnapshot querySnapshot = query.get().get();
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    Post post = documentSnapshot.toObject(Post.class);
                    post.setId(documentSnapshot.getId());
                    posts.add(post);
                }

                return posts.size() > 0 ? posts : null;
        } catch (Exception e) {
            LOG.error("Failed to get user posts from database: " + e.getMessage());
            return null;
        }
    }

    public Ratings GetRatingsByUserId (String userId){
        Ratings ratings = null;
        try {
            Query query = firebase.getDb().collection("ratings").whereEqualTo("UserId", userId);
            QuerySnapshot querySnapshot = query.get().get();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                ratings = documentSnapshot.toObject(Ratings.class);
                ratings.setId(documentSnapshot.getId());
                return ratings;
            }

            // Item was not found, return a default ratings (it is not currently present in the db)
            return new Ratings(new ArrayList<UserRating>(), userId);
        }
        catch (Exception e) {
            LOG.error("Failed to get user ratings from database: " + e.getMessage());
            return null;
        }
    }
}
