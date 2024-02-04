package com.example.certificateGenerator.service;

import com.example.certificateGenerator.entity.Recipient;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class MultipleCertificate implements Runnable{

    private Map<Long,ByteArrayOutputStream> certArrayOutputStreamList;
    private Recipient recipient;
    private final FileService fileService;

    public MultipleCertificate(Map<Long,ByteArrayOutputStream> certArrayOutputStreamList, FileService fileService) {
        this.certArrayOutputStreamList = certArrayOutputStreamList;
        this.fileService = fileService;
    }

    public synchronized Map<Long,ByteArrayOutputStream> getCertArrayOutputStreamList() {
        return certArrayOutputStreamList;
    }

    public synchronized Recipient getRecipient() {
        return recipient;
    }

    public synchronized  void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    @Override
    public void run() {

        synchronized (this){
            ByteArrayOutputStream certificate = fileService.generateCertificatePdf(getRecipient().getId());
            getCertArrayOutputStreamList().put( getRecipient().getId(), certificate);
        }


    }




}
