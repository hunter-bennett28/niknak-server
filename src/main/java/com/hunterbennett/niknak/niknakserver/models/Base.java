package com.hunterbennett.niknak.niknakserver.models;

import com.google.cloud.firestore.annotation.PropertyName;

import lombok.Data;

/**
 * A Base class for all models, ensuring all Models have an id value
 */
@Data
public class Base {
    @PropertyName("Id")
    protected String id;
}
