package com.hunterbennett.niknak.niknakserver.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.cloud.firestore.annotation.PropertyName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A model representing a report made against some site content
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Report extends Base {
    @PropertyName("UserID")
    private String userID;
    @PropertyName("ReportedEntityID")
    private String reportedEntityID;
    @PropertyName("Reason")
    private String reason;
    @PropertyName("Description")
    private String description;
    @PropertyName("Type")
    private String type;
    @PropertyName("Date")
    private String date;

    public Report(String id) {
        this.id = id;
    }
}
