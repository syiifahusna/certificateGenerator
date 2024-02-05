package com.example.certificateGenerator.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class FileControllerAdvice {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleMultipartException(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(new ErrorDetails("Not Acceptable", ex.getMessage()));
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity handleUploadException(UploadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDetails("Internal Server Error", ex.getMessage()));
    }

    @ExceptionHandler(PathNotFoundException.class)
    public ResponseEntity handlePathNotFoundException(PathNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorDetails("Not Found", ex.getMessage()));
    }
}
