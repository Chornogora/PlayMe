package com.dataart.playme.repository.impl;

import java.util.Map;

public abstract class AbstractConstraintRepository {

    protected static final String ASCENDING = "ASC";

    protected static final String DESCENDING = "DESC";

    protected static final Map<String, String> SORTING_TYPES = Map.of(
            "ASC", "ASC",
            "DESC", "DESC");
}
