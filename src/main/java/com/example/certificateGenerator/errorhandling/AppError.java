package com.example.certificateGenerator.errorhandling;


import java.time.Instant;

public class AppError {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public AppError(String error) {
        this.error = error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //    {
//        "timestamp": "2024-01-25T09:28:38.162+00:00",
//            "status": 404,
//            "error": "Not Found",
//            "path": "/api.certificategenerator/processfil"
//    }
}
