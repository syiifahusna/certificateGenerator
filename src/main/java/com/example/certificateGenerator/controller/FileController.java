package com.example.certificateGenerator.controller;

import com.example.certificateGenerator.entity.Certificate;
import com.example.certificateGenerator.entity.FileUpload;
import com.example.certificateGenerator.entity.Recipient;
import com.example.certificateGenerator.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api.certificategenerator")
public class FileController {

    private final FileService fileService;
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2 MB

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String test(){
        return "success";
    }

    @PostMapping("/processfile")
    public ResponseEntity<Object> processFile(@RequestPart("file") MultipartFile file,
                                              @RequestParam("certOf") String certOf,
                                              @RequestParam("desc1") String desc1,
                                              @RequestParam("desc2") String desc2,
                                              @RequestParam("eventName") String eventName,
                                              @RequestParam("organizer") String organizer,
                                              @RequestParam("desc3") String desc3,
                                              @RequestParam("desc4") String desc4,
                                              @RequestPart("signImage") MultipartFile signImage,
                                              @RequestParam("issuerName") String issuerName,
                                              @RequestParam("issuerTitle") String issuerTitle) throws IOException {

        //file filter
        if(file.isEmpty()){
            throw new MultipartException("File is empty");
        }
        if (!file.getOriginalFilename().toLowerCase().endsWith(".xls") && !file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new MultipartException("Only xls or xlsx files are allowed.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MultipartException("File size exceeds the maximum allowed limit (2 MB).");
        }

        //image filter
        if (signImage.isEmpty()) {
            throw new MultipartException("Image is null");
        }

        if (!signImage.getOriginalFilename().toLowerCase().endsWith(".png")) {
            throw new MultipartException("Only png files are allowed.");
        }
        if (signImage.getSize() > MAX_FILE_SIZE) {
            throw new MultipartException("File size exceeds the maximum allowed limit (2 MB).");
        }

        Path tempFilePath = Files.createTempFile("temp-", "-" + signImage.getOriginalFilename());
        Files.copy(signImage.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        File imageFile = tempFilePath.toFile();
        imageFile.deleteOnExit();

        Certificate certificate = new Certificate(  certOf,
                                                   desc1,
                                                   desc2,
                                                   eventName,
                                                   organizer,
                                                   desc3,
                                                   desc4,
                                                   imageFile,
                                                   issuerName,
                                                   issuerTitle);

        List<Recipient> recipients = fileService.processFileContent(file,certificate);
        FileUpload fileUpload = new FileUpload("success", LocalDate.now(), recipients);
        return ResponseEntity.ok().body(fileUpload);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> generateCertificate(@PathVariable("id") Long id) {

        ByteArrayOutputStream baos = fileService.generateCertificatePdf(id);

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);


        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(new ByteArrayResource(pdfBytes));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable("id") Long id){
        ByteArrayOutputStream baos = fileService.generateCertificatePdf(id);
        Recipient recipient = fileService.getRecipient(id);

        byte[] pdfBytes = baos.toByteArray();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+recipient.getName()+".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(new ByteArrayResource(pdfBytes));
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadCertificates(){

        ByteArrayOutputStream zipStream = fileService.downloadCertificates();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "generateCert_"+LocalDate.now()+".zip");

        return ResponseEntity.ok()
                .headers(headers)
                .body(zipStream.toByteArray());
    }

    @GetMapping("example/download")
    public ResponseEntity<byte[]> downloadXlsx(){

        ByteArrayOutputStream outputStream = fileService.generateExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "certNameList.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());


    }



}
