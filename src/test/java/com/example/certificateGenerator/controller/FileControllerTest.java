package com.example.certificateGenerator.controller;

import com.example.certificateGenerator.entity.Recipient;
import com.example.certificateGenerator.entity.UploadDetails;
import com.example.certificateGenerator.errorhandling.PathNotFoundException;
import com.example.certificateGenerator.errorhandling.UploadException;
import com.example.certificateGenerator.service.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    List<Recipient> recipientList;

    @BeforeEach
    void setUp() {

        recipientList = new LinkedList<>();
        recipientList.add(new Recipient(1L,"Yuuji Itadori", LocalDate.of(2022,11,20)));
        recipientList.add(new Recipient(2L,"Satoru Gojo", LocalDate.of(2022,11,21)));
        recipientList.add(new Recipient(3L,"Megumi Fushiguro",LocalDate.of(2022,11,22)));

    }

    @Test
    void processFile() throws IOException {
        // Given
        MultipartFile file = new MockMultipartFile("file", "test.xlsx", "application/vnd.ms-excel", "testFile".getBytes());
        MultipartFile signImage = new MockMultipartFile("signImage", "image.png", "image/png", "testImage".getBytes());

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

        ByteArrayOutputStream mockOutputStream = new ByteArrayOutputStream();
        byte[] mockByte = mockOutputStream.toByteArray();

        Mockito.when(fileService.generateCertificatePdf(1L)).thenReturn(mockOutputStream);

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_PDF);
        expectedHeaders.setContentLength(mockByte.length);


        ResponseEntity<Object> response = fileController.generateCertificate(1L);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).isInstanceOf(ByteArrayResource.class);
    }

    @Test
    public void testDownloadCertificate() {

        Mockito.when(fileService.generateCertificatePdf(ArgumentMatchers.anyLong())).thenReturn(new ByteArrayOutputStream()); // Mock the response
        Mockito.when(fileService.getRecipient(ArgumentMatchers.anyLong())).thenReturn(recipientList.get(0)); // Mock recipient

        ResponseEntity<Resource> response = fileController.downloadCertificate(1L);

        HttpHeaders headers = response.getHeaders();
        byte[] body = ((ByteArrayResource) response.getBody()).getByteArray();

        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(headers.getContentLength()).isNotNull().isEqualTo(body.length);
        Assertions.assertThat(headers.getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo("attachment; filename="+recipientList.get(0).getName()+".pdf");

    }

    @Test
    void downloadCertificates() {

        ByteArrayOutputStream mockOutputStream = Mockito.mock(ByteArrayOutputStream.class);
        Mockito.when(fileService.downloadCertificates()).thenReturn(mockOutputStream); // Mock the response

        ResponseEntity<byte[]> response = fileController.downloadCertificates();

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        expectedHeaders.setContentDispositionFormData("attachment", "generateCert_"+LocalDate.now()+".zip");

        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals(mockOutputStream.toByteArray(), response.getBody());

    }

    @Test
    void downloadXlsx() throws IOException {

        ByteArrayOutputStream mockOutputStream = Mockito.mock(ByteArrayOutputStream.class);
        Mockito.when(fileService.generateExcel()).thenReturn(mockOutputStream);

        ResponseEntity<byte[]> response = fileController.downloadXlsx();

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        expectedHeaders.setContentDispositionFormData("attachment", "certNameList.xlsx");

        assertEquals(expectedHeaders, response.getHeaders());
        assertEquals(mockOutputStream.toByteArray(), response.getBody());
    }

    @Test
    void downloadXlsx_throwException() throws IOException {

        Mockito.when(fileService.generateExcel()).thenThrow(new IOException());
        assertThrows(UploadException.class, () -> fileController.downloadXlsx());

    }
}