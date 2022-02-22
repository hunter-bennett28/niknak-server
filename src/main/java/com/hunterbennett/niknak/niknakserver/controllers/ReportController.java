package com.hunterbennett.niknak.niknakserver.controllers;

import java.util.List;
import com.hunterbennett.niknak.niknakserver.models.Report;
import com.hunterbennett.niknak.niknakserver.repositories.ReportRepository;
import com.hunterbennett.niknak.niknakserver.util.NikNakUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/reports")
public class ReportController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);
    private final ReportRepository reportRepo;

    public ReportController(ReportRepository reportRepo) {
        this.reportRepo = reportRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable(required = true, name = "id") String id) {
        try {
            Report report = new Report();
            report.setId(id);
            report = reportRepo.get(report);
            return new ResponseEntity<Report>(reportRepo.get(report), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to get report: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        try {
            return new ResponseEntity<>(reportRepo.getAll(), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to get reports: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Report> addReport(@RequestBody Report report) {
        try {
            report.setDate(NikNakUtils.getCurrentDateString());

            //Check to make sure the report is unique (no dupicate reportEntityID and userId
            List<Report> allReports = reportRepo.getAll();
            for (Report r : allReports) {
                if (r.getUserID() == report.getUserID() && r.getReportedEntityID() == report.getReportedEntityID()
                    && r.getType() == report.getType()) {
                    // If matching, update the old one and replace it with the new one
                    report.setId(r.getId());
                    reportRepo.update(report);
                    return new ResponseEntity<>(report, HttpStatus.OK);
                }
            }
    
            return new ResponseEntity<>(reportRepo.add(report), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to add report: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReport(@PathVariable( required = true) String id) {
        try {
            Report report = reportRepo.get(new Report(id));
            return new ResponseEntity<>(reportRepo.delete(report), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error("Controller failed to delet report: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
