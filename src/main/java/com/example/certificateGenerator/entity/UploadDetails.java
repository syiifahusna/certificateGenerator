package com.example.certificateGenerator.entity;

import java.time.LocalDate;
import java.util.List;

public class UploadDetails {

    private String status;
    private LocalDate uploadDate;
    private List<Recipient> body;

    public UploadDetails(String status, LocalDate uploadDate, List<Recipient> body) {
        this.status = status;
        this.uploadDate = uploadDate;
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public List<Recipient> getBody() {
        return body;
    }
}
