package com.example.certificateGenerator.service;

import com.example.certificateGenerator.aspect.LoggingAspect;
import com.example.certificateGenerator.entity.Recipient;
import com.example.certificateGenerator.errorhandling.UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class MultipleCertificate implements Runnable{

    Logger logger = LoggerFactory.getLogger(MultipleCertificate.class);

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
            try{
                logger.info("Running thread : " + Thread.currentThread().getName());
                ByteArrayOutputStream certificate = fileService.generateCertificatePdf(getRecipient().getId());
                getCertArrayOutputStreamList().put( getRecipient().getId(), certificate);
            }catch(RuntimeException e){
                throw new UploadException("Thread " + Thread.currentThread().getName() + " failed to finished");
            }

        }

    }




}
