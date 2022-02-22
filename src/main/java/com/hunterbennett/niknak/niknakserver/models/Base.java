package com.hunterbennett.niknak.niknakserver.models;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.Data;

@Data
public class Base {
    @PropertyName("Id")
    protected String id;
}
