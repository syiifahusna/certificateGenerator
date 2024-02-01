package com.example.certificateGenerator.entity;

import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Recipient {

    private Long id;

    private String name;
    private LocalDate issuedDate;

    public Recipient(){}

    public Recipient(Long id, String name, LocalDate issuedDate) {
        this.id = id;
        this.name = name;
        this.issuedDate = issuedDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }
}
