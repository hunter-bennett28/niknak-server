package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;
import com.hunterbennett.niknak.niknakserver.models.Conversation;
import com.hunterbennett.niknak.niknakserver.models.Report;

import org.springframework.stereotype.Repository;

@Repository
public class ConversationRepository implements IFirestoreRepository<Conversation> {
    private final String COLLECTION = "conversations";
    private final BaseRepository repo;
    private final ReportRepository reportRepo;

    public ConversationRepository(BaseRepository baseRepository, ReportRepository reportRepo) throws Exception {
        //this.repo = new BaseRepository(COLLECTION);
        this.repo = baseRepository;
        this.reportRepo = reportRepo;
    }

    @Override
    public Conversation add(Conversation conversation) {
        return repo.add(COLLECTION, conversation);
    }

    @Override
    public boolean delete(Conversation conversation) {
        List<Report> reports = reportRepo.getAll();

        // Delete any reports associated with this conversation
        reports.forEach((report) -> {
            if (report.getReportedEntityID() == conversation.getId() && report.getType() == "Chat") {
                reportRepo.delete(report);
            }
        });

        return repo.delete(COLLECTION, conversation);
    }

    @Override
    public Conversation get(Conversation conversation) {
        return repo.get(COLLECTION, conversation, Conversation.class);
    }

    @Override
    public List<Conversation> getAll() {
        return repo.getAll(COLLECTION, Conversation.class);
    }

    @Override
    public boolean update(Conversation conversation) {
        return repo.update(COLLECTION, conversation);
    }

    public List<Conversation> getUserConversations(String userId) {
        return repo.getUserConversations(userId);
    }
}
