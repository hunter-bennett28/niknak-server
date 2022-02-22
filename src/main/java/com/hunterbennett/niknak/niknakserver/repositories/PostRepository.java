package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;
import com.hunterbennett.niknak.niknakserver.models.Post;
import com.hunterbennett.niknak.niknakserver.models.Report;

import org.springframework.stereotype.Repository;

@Repository
public class PostRepository implements IFirestoreRepository<Post> {
    private final String COLLECTION = "posts";
    private final BaseRepository repo;
    private final ReportRepository reportRepo;

    public PostRepository(BaseRepository baseRepository, ReportRepository reportRepo) throws Exception {
        this.repo = baseRepository;
        this.reportRepo = reportRepo;
    }

    @Override
    public Post add(Post post) {
        return repo.add(COLLECTION, post);
    }

    @Override
    public boolean delete(Post post) {
        List<Report> reports = reportRepo.getAll();

        // Delete any reports associated with this post
        reports.forEach((report) -> {
            if (report.getReportedEntityID() == post.getId() && report.getType() == "Chat") {
                reportRepo.delete(report);
            }
        });

        return repo.delete(COLLECTION, post);
    } 

    @Override
    public Post get(Post post) {
        return repo.get(COLLECTION, post, Post.class);
    }

    @Override
    public List<Post> getAll() {
        return repo.getAll(COLLECTION, Post.class);
    }

    @Override
    public boolean update(Post post) {
        return repo.update(COLLECTION, post);
    }

    public List<Post> getUserPosts(String userId) {
        return repo.getUserPosts(userId);
    }
}
