package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;

import com.hunterbennett.niknak.niknakserver.firebase.Admin;
import com.hunterbennett.niknak.niknakserver.models.Conversation;
import com.hunterbennett.niknak.niknakserver.models.Post;
import com.hunterbennett.niknak.niknakserver.models.Report;
import com.hunterbennett.niknak.niknakserver.models.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IFirestoreRepository<User> {
    private final String COLLECTION = "users";
    private final BaseRepository repo;
    private final ConversationRepository conversationRepo;
    private final PostRepository postRepo;
    private final ReportRepository reportRepo;

    

    public UserRepository(BaseRepository baseRepository, ConversationRepository conversationRepo, PostRepository postRepo,
            ReportRepository reportRepo) throws Exception {
        // this.repo = new BaseRepository(COLLECTION);
        this.repo = baseRepository;
        // this.repo.setCollection(COLLECTION);
        this.conversationRepo = conversationRepo;
        this.postRepo = postRepo;
        this.reportRepo = reportRepo;
    }

    @Override
    public User get(User record) {
        User user = repo.get(COLLECTION, record, User.class);
        if (user == null)
            return null;

        user.setIsAdmin(Admin.adminIds.contains(user.getId()));
        return user;
    }

    @Override
    public List<User> getAll() {
        return repo.getAll(COLLECTION, User.class);
    }

    @Override
    public User add(User record) {
        return repo.add(COLLECTION, record);
    }

    @Override
    public boolean update(User record) {
        return repo.update(COLLECTION, record);
    }

    @Override
    public boolean delete(User record) {
        // Delete all conversations the user is a part of
        List<Conversation> conversations = conversationRepo.getUserConversations(record.getId());
        if (conversations != null) {
            conversations.forEach(convo -> { conversationRepo.delete(convo); });
        }

        // Delete all of the user's posts
        List<Post> posts = postRepo.getUserPosts(record.getId());
        if (posts != null) {
            posts.forEach(post -> { postRepo.delete(post); });
        }

        // Delete all reports on the user
        List<Report> reports = reportRepo.getAll();
        reports.forEach(report -> {
            if (report.getReportedEntityID() == record.getId() && report.getType() == "Chat") {
                reportRepo.delete(report);
            }
        });
    
        return repo.delete(COLLECTION, record);
    }
}
