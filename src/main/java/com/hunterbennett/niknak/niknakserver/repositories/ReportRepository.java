package com.hunterbennett.niknak.niknakserver.repositories;

import java.util.List;

import com.hunterbennett.niknak.niknakserver.models.Report;

import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository implements IFirestoreRepository<Report> {
    private final String COLLECTION = "reports";
    private final BaseRepository repo;

    public ReportRepository(BaseRepository baseRepository) throws Exception {
        // this.repo = new BaseRepository(COLLECTION);
        this.repo = baseRepository;
        // this.repo.setCollection(COLLECTION);
    }

    @Override
    public Report get(Report report) {
        System.out.println("Getting report " + report.getId());
        return repo.get(COLLECTION, report, Report.class);
    }

    @Override
    public List<Report> getAll() {
        return repo.getAll(COLLECTION, Report.class);
    }

    @Override
    public Report add(Report report) {
        return repo.add(COLLECTION, report);
    }

    @Override
    public boolean update(Report report) {
        return repo.update(COLLECTION, report);
    }

    @Override
    public boolean delete(Report report) {
        return repo.delete(COLLECTION, report);
    }
}
