package com.example.certificateGenerator.service;

import com.example.certificateGenerator.entity.Recipient;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

@Component
public class MultipleCertificate implements Runnable{


    private List<ByteArrayOutputStream> certArrayOutputStreamList;
    private Recipient recipient;

    private final FileService fileService;

    private List<String> recipientName;


    public MultipleCertificate(List<ByteArrayOutputStream> certArrayOutputStreamList, FileService fileService, List<String> recipientName) {
        this.certArrayOutputStreamList = certArrayOutputStreamList;
        this.fileService = fileService;
        this.recipientName = recipientName;
    }

    public List<ByteArrayOutputStream> getCertArrayOutputStreamList() {
        return certArrayOutputStreamList;
    }

    public synchronized void setCertArrayOutputStreamList(List<ByteArrayOutputStream> certArrayOutputStreamList) {
        this.certArrayOutputStreamList = certArrayOutputStreamList;
    }

    public synchronized Recipient getRecipient() {
        return recipient;
    }

    public synchronized  void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public synchronized void setRecipientName(List<String> recipientName) {
        this.recipientName = recipientName;
    }

    @Override
    public void run() {

        synchronized (this){
            ByteArrayOutputStream certificate = fileService.generateCertificatePdf(recipient.getId());
            certArrayOutputStreamList.add(certificate);
            recipientName.add(recipient.getName());
            System.out.println(recipient.getName());
        }


    }




}
