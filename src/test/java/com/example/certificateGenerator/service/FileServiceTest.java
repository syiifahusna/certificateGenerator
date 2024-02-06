package com.example.certificateGenerator.service;

import com.example.certificateGenerator.entity.Certificate;
import com.example.certificateGenerator.entity.Recipient;
import com.example.certificateGenerator.service.implementation.FileServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    FileService fileService;

    @InjectMocks
    FileServiceImp fileServiceImp;

    List<Recipient> recipientList;

    @BeforeEach
    void setUp() {

        recipientList = new LinkedList<>();
        recipientList.add(new Recipient(1L,"Yuuji Itadori",LocalDate.of(2022,11,20)));
        recipientList.add(new Recipient(2L,"Satoru Gojo", LocalDate.of(2022,11,21)));
        recipientList.add(new Recipient(3L,"Megumi Fushiguro",LocalDate.of(2022,11,22)));

    }

    @AfterEach
    void tearDown() {
        recipientList = null;
    }

    @Test
    void processFileContent() throws IOException {

        //Mock file
        File mockFile = ResourceUtils.getFile("files/test/nameList.xlsx");
        byte[] mockFileContent = Files.readAllBytes(mockFile.toPath());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nameList.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                mockFileContent
        );

        // Mock Image
        File mockImage = ResourceUtils.getFile("files/test/sign.png");
        Certificate mockCertificate = new Certificate(
        "test",
        "test",
        "test",
        "test",
        "test",
        "test",
        "test",
        mockImage,
        "test",
        "test"
        );

        Mockito.when(fileService.processFileContent(file, mockCertificate)).thenReturn(recipientList);
        List<Recipient> result = fileService.processFileContent(file, mockCertificate);
        org.junit.jupiter.api.Assertions.assertEquals(recipientList,result);
    }

    @Test
    void processFileContent_wrongTitle() throws IOException{

        //Mock file
        File mockFile = ResourceUtils.getFile("files/test/nameList_wrongTitle.xlsx");
        byte[] mockFileContent = Files.readAllBytes(mockFile.toPath());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nameList_wrongTitle.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                mockFileContent
        );

        // Mock Image
        File mockImage = ResourceUtils.getFile("files/test/sign.png");
        Certificate mockCertificate = new Certificate(
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                mockImage,
                "test",
                "test"
        );

        Mockito.when(fileService.processFileContent(file, mockCertificate)).thenThrow(new IOException("Wrong name for column id"));
        try{
            fileService.processFileContent(file, mockCertificate);
        }catch(IOException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("Wrong name for column id");
        }

    }

    @Test
    void processFileContent_wrongDataType() throws IOException{
        //Mock file
        File mockFile = ResourceUtils.getFile("files/test/nameList_wrongDataType.xlsx");
        byte[] mockFileContent = Files.readAllBytes(mockFile.toPath());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nameList_wrongDataType.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                mockFileContent
        );

        // Mock Image
        File mockImage = ResourceUtils.getFile("files/test/sign.png");
        Certificate mockCertificate = new Certificate(
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                mockImage,
                "test",
                "test"
        );

        Mockito.when(fileService.processFileContent(file, mockCertificate)).thenThrow(new IOException("Wrong column id data type"));
        try{
            fileService.processFileContent(file, mockCertificate);
        }catch(IOException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("Wrong column id data type");
        }
    }

    @Test
    void processFileContent_emptyNameList() throws IOException{
        //Mock file
        File mockFile = ResourceUtils.getFile("files/test/nameList_emptyNameList.xlsx");
        byte[] mockFileContent = Files.readAllBytes(mockFile.toPath());
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "nameList_emptyNameList.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                mockFileContent
        );

        // Mock Image
        File mockImage = ResourceUtils.getFile("files/test/sign.png");
        Certificate mockCertificate = new Certificate(
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                mockImage,
                "test",
                "test"
        );

        Mockito.when(fileService.processFileContent(file, mockCertificate)).thenThrow(new NullPointerException("No recipient found"));
        try{
            fileService.processFileContent(file, mockCertificate);
        }catch(NullPointerException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("No recipient found");
        }
    }

    @Test
    void generateCertificatePdf() {
        fileServiceImp.setRecipients(recipientList);
        File mockImage = Mockito.mock(File.class);
        fileServiceImp.setCertificate(new Certificate(
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                mockImage,
                "test",
                "test"
        ));
        ByteArrayOutputStream generateCertificate = fileServiceImp.generateCertificatePdf(recipientList.get(0).getId());
        Mockito.when(fileService.generateCertificatePdf(1L)).thenReturn(generateCertificate);

        ByteArrayOutputStream result = fileService.generateCertificatePdf(1L);

        Assertions.assertThat(result).isEqualTo(generateCertificate);
    }

    @Test
    void generateCertificatePdf_recipientEmpty() {
        Mockito.when(fileService.generateCertificatePdf(10L))
                .thenThrow(new NullPointerException("No recipient found"));

        // Call the method under test and expect an exception
        NullPointerException result = org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> fileService.generateCertificatePdf(10L)
        );

        Assertions.assertThat(result.getMessage()).isEqualTo("No recipient found");
    }

    @Test
    void downloadCertificates() {
        File mockImage = null;

        // Mock Image
        try{
            mockImage = ResourceUtils.getFile("files/test/sign.png");
        }catch(IOException e){
            e.printStackTrace();
        }

        Certificate mockCertificate = new Certificate(
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                mockImage,
                "test",
                "test"
        );

        fileServiceImp.setRecipients(recipientList);
        fileServiceImp.setCertificate(mockCertificate);
        ByteArrayOutputStream generateCertificates = fileServiceImp.downloadCertificates();
        Mockito.when(fileService.downloadCertificates()).thenReturn(generateCertificates);

        ByteArrayOutputStream result = fileService.downloadCertificates();
        Assertions.assertThat(result).isEqualTo(generateCertificates);

    }

    @Test
    void downloadCertificates_recipientEmpty() {

        Mockito.when(fileService.downloadCertificates())
                .thenThrow(new NullPointerException("No recipient found"));

        // Call the method under test and expect an exception
        NullPointerException result = org.junit.jupiter.api.Assertions.assertThrows(
                NullPointerException.class,
                () -> fileService.downloadCertificates()
        );

        Assertions.assertThat(result.getMessage()).isEqualTo("No recipient found");
    }

    @Test
    void generateExcel() throws IOException {

        //setup
        ByteArrayOutputStream generateExcel = fileServiceImp.generateExcel();
        Mockito.when(fileService.generateExcel()).thenReturn(generateExcel);

        //when
        ByteArrayOutputStream result = fileService.generateExcel();

        //then
        org.junit.jupiter.api.Assertions.assertEquals(generateExcel,result);

    }

    @Test
    void getRecipientTest() {

        //mock the action of fileService.getRecipient()
        //what it would do if parameter 1L
        Mockito.when(fileService.getRecipient(1L)).thenReturn(recipientList.get(0));

        //perform the action
        Recipient result = fileService.getRecipient(1L);

        //result to be expected
        Assertions.assertThat(result).isEqualTo(recipientList.get(0));

    }

    @Test
    void getRecipientTest_dataEmpty() {
        Mockito.when(fileService.getRecipient(10L)).thenReturn(null);
        //perform the action
        Recipient result = fileService.getRecipient(10L);
        //result to be expected
        Assertions.assertThat(result).isEqualTo(null);

    }
}