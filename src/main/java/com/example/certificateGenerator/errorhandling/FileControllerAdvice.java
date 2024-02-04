package com.example.certificateGenerator.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class FileControllerAdvice {

    ErrorDetails ed = null;

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleMultipartException(Exception ex){
        ed = new ErrorDetails("Not Acceptable",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ed);
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity handleUploadException(Exception ex){
        ed = new ErrorDetails("Internal Server Error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ed);
    }

}
