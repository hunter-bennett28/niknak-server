package com.hunterbennett.niknak.niknakserver.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Report extends Base {
    private String UserID;
    private String ReportedEntityID;
    private String Reason;
    private String Description;
    private String Type;
    private String Date;

    public Report(String id) {
        this.Id = id;
    }
}
