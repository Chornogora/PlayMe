package com.dataart.playme.exception;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> issues;

    public ValidationException(List<String> issues) {
        this.issues = issues;
    }

    public List<String> getIssues() {
        return issues;
    }
}
