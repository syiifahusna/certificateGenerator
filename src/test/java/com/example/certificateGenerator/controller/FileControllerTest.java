package com.example.certificateGenerator.controller;

import com.example.certificateGenerator.entity.UploadDetails;
import com.example.certificateGenerator.service.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @Test
    void processFile() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", "test data".getBytes());
        MultipartFile signImage = new MockMultipartFile("signImage", "image.png", "image/png", "image data".getBytes());

        Mockito.when(fileService.processFileContent(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Collections.emptyList());

        // When
        ResponseEntity<Object> response = fileController.processFile(file, "certOf", "desc1", "desc2", "eventName",
                "organizer", "desc3", "desc4", signImage, "issuerName", "issuerTitle");

        // Then
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isInstanceOf(UploadDetails.class);
    }

    @Test
    void generateCertificate() {
    }

    @Test
    void downloadCertificate() {
    }

    @Test
    void downloadCertificates() {
    }

    @Test
    void downloadXlsx() {
    }
}