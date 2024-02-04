package com.example.certificateGenerator.errorhandling;

public class ErrorDetails {

    private String error;
    private String cause;

    public ErrorDetails(String error, String cause) {
        this.error = error;
        this.cause = cause;
    }

    public String getError() {
        return error;
    }

    public String getCause() {
        return cause;
    }
}
