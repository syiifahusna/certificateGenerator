package com.example.certificateGenerator.errorhandling;

public class UploadException extends RuntimeException{

    public UploadException(String message) {
        super(message);
    }
}
