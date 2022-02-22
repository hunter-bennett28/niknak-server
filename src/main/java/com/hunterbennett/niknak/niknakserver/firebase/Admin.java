package com.hunterbennett.niknak.niknakserver.firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A very unsecure and stopgap class for designating a user as having admin priviledges.
 * This creates a list of userIds that are considered admins for querying against.
 * This is just for proof of product, and is recognized to be poor style.
 */
public class Admin {
    public static List<String> adminIds = new ArrayList<>(Arrays.asList(
        "7BUOhqRuc8Mg4I4B3NNm",
        "t6TsYax9I7fUHXyLJpbk"
    ));
}
