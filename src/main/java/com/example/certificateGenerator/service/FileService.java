package com.example.certificateGenerator.service;

import com.example.certificateGenerator.entity.Certificate;
import com.example.certificateGenerator.entity.Recipient;
import com.example.certificateGenerator.errorhandling.UploadException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {

    private List<Recipient> recipients;
    private Certificate certificate;

    File fileBgImage = new File("files/img/cert_bg.jpg");
    File fileFont1 = new File("files/fonts/CaviarDreams.ttf");
    File fileFont2 =  new File("files/fonts/Julietta.ttf");

    Color colorText = Color.decode("#71644d");

    float x = 0; // X coordinate of the bg image
    float y = 0; // Y coordinate of the bg image
    float width = 612; // Width of the bg image
    float height = 792; // Height of the bg image

    //font
    int fontSize40 = 40;
    int fontSize20 = 20;
    int fontSize12 = 12;
    float pageWidth = 612;

    public List<Recipient> processFileContent(MultipartFile file, Certificate certificate) throws IOException {

            this.recipients = new LinkedList<>();
            this.certificate = certificate;

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            int iterationCount = 0;

            //check first row
            if (iterator.hasNext()) {
                Row currentRow = iterator.next();

                if (!currentRow.getCell(0).getStringCellValue().equals("id")) {
                    throw new IOException("Wrong name for column id");
                }

                if (!currentRow.getCell(1).getStringCellValue().equals("name")) {
                    throw new IOException("Wrong name for column name");
                }

                if (!currentRow.getCell(2).getStringCellValue().equals("date issued")) {
                    throw new IOException("Wrong name for column date issued");
                }

            }

            // Extract data
            while (iterator.hasNext() && iterationCount<100) {
                Recipient recipient = new Recipient();
                Row currentRow = iterator.next();

                if (currentRow.getCell(0).getCellType() == CellType.NUMERIC) {
                    recipient.setId(Double.valueOf(currentRow.getCell(0).getNumericCellValue()).longValue());
                } else {
                    throw new IOException("Wrong column id data type");
                }

                if (currentRow.getCell(1).getCellType() == CellType.STRING) {
                    recipient.setName(currentRow.getCell(1).getStringCellValue());
                } else {
                    throw new IOException("Wrong column name data type");
                }

                if (currentRow.getCell(2).getDateCellValue() != null) {
                    Date date = currentRow.getCell(2).getDateCellValue();
                    Instant instant = date.toInstant();
                    LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    recipient.setIssuedDate(localDate);
                }else{
                    throw new IOException("Wrong column date data type");
                }

                recipients.add(recipient);
                iterationCount++;

            }

            workbook.close();
            return recipients;

    }

    public ByteArrayOutputStream generateCertificatePdf(Long id) {

        Recipient recipient = getRecipient(id);

        if(recipient == null){
            throw new NullPointerException("No recipient Found");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(0));

            PDType0Font font = PDType0Font.load(document, fileFont1);
            PDType0Font font2 = PDType0Font.load(document, fileFont2);

            //image background
            BufferedImage bufferedImage = ImageIO.read(fileBgImage);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            contentStream.drawImage(pdImage, x, y, width, height);


            //text display
            String txtCertificate = "CERTIFICATE";
            float txtCertificateWidth = font.getStringWidth(txtCertificate) * fontSize40 / 1000f;
            float centerXtxtCertificate = (pageWidth - txtCertificateWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize40);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtCertificate, 680);
            contentStream.showText(txtCertificate);
            contentStream.endText();

            //text display
            String txtOfParticipation = "OF " + certificate.getCertOf();
            float txtOfParticipationWidth = font.getStringWidth(txtOfParticipation) * fontSize20 / 1000f;
            float centerXtxtOfParticipation = (pageWidth - txtOfParticipationWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize20);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtOfParticipation, 640);
            contentStream.showText(txtOfParticipation);
            contentStream.endText();


            //text display
            String txtAwardTo = certificate.getDesc1();
            float txtAwardToWidth = font.getStringWidth(txtAwardTo) * fontSize12 / 1000f;
            float centerXtxtAwardTo = (pageWidth - txtAwardToWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtAwardTo, 600);
            contentStream.showText(txtAwardTo);
            contentStream.endText();

            //text display
            String txtRecipient = recipient.getName();
            float txtRecipientWidth = font2.getStringWidth(txtRecipient) * fontSize40 / 1000f;
            float centerXtxtRecipient = (pageWidth - txtRecipientWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font2,fontSize40);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtRecipient, 540);
            contentStream.showText(txtRecipient);
            contentStream.endText();

            //text display
            String txtDescription = certificate.getDesc2();
            float txtDescriptionWidth = font.getStringWidth(txtDescription) * fontSize12 / 1000f;
            float centerXtxtDescription = (pageWidth - txtDescriptionWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtDescription, 500);
            contentStream.showText(txtDescription);
            contentStream.endText();

            //text display
            String txtEventName = certificate.getEventName();
            float txtEventNameWidth = font.getStringWidth(txtEventName) * fontSize12 / 1000f;
            float centerXtxtEventName = (pageWidth - txtEventNameWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtEventName, 480);
            contentStream.showText(txtEventName);
            contentStream.endText();

            //text display
            String txtOrganizeBy = "ORGANIZED BY";
            float txtOrganizeByWidth = font.getStringWidth(txtOrganizeBy) * fontSize12 / 1000f;
            float centerXtxtOrganizeBy = (pageWidth - txtOrganizeByWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtOrganizeBy, 460);
            contentStream.showText(txtOrganizeBy);
            contentStream.endText();

            //text display
            String txtOrganizer = certificate.getOrganizer();
            float txtOrganizerWidth = font.getStringWidth(txtOrganizer) * fontSize12 / 1000f;
            float centerXtxtOrganizer = (pageWidth - txtOrganizerWidth) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtOrganizer, 440);
            contentStream.showText(txtOrganizer);
            contentStream.endText();

            //text display
            String txtDescription2 = certificate.getDesc3();
            float txtDescription2Width = font.getStringWidth(txtDescription2) * fontSize12 / 1000f;
            float centerXtxtDescription2 = (pageWidth - txtDescription2Width) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtDescription2, 420);
            contentStream.showText(txtDescription2);
            contentStream.endText();

            //text display
            String txtDescription3 = certificate.getDesc4();
            float txtDescription3Width = font.getStringWidth(txtDescription3) * fontSize12 / 1000f;
            float centerXtxtDescription3 = (pageWidth - txtDescription3Width) / 2;
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(centerXtxtDescription3, 400);
            contentStream.showText(txtDescription3);
            contentStream.endText();


            BufferedImage bufferedImage2 = ImageIO.read(certificate.getSignImage());
            PDImageXObject pdImage2 = LosslessFactory.createFromImage(document, bufferedImage2);
            float x2 = 100; // X coordinate of the image
            float y2 = 180; // Y coordinate of the image
            float width2 = 100; // Width of the image
            float height2 = 100; // Height of the image
            // Draw the image on the page
            contentStream.drawImage(pdImage2, x2, y2, width2, height2);

            //text display
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(100, 180);
            contentStream.showText("____________________________");
            contentStream.endText();

            //text display
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(100, 160);
            contentStream.showText(certificate.getIssuerName());
            contentStream.endText();

            //text display
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(100, 140);
            contentStream.showText(certificate.getIssuerTitle());
            contentStream.endText();

            //text display
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = recipient.getIssuedDate().format(formatter);
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(350, 200);
            contentStream.showText(formattedDate);
            contentStream.endText();

            //text display
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(350, 180);
            contentStream.showText("____________________________");
            contentStream.endText();

            //text display
            contentStream.beginText();
            contentStream.setFont(font,fontSize12);
            contentStream.setNonStrokingColor(colorText);
            contentStream.newLineAtOffset(350, 160);
            contentStream.showText("AWARDED ON");
            contentStream.endText();

            contentStream.close();
            document.save(baos);
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos;
    }

    public ByteArrayOutputStream downloadCertificates() {
        //Generate Certificate faster using thread
        Map<Long,ByteArrayOutputStream> certArrayOutputStreamList = new HashMap<>();
        Thread[] threads = new Thread[recipients.size()];
        for (int i =0;i<recipients.size();i++){
            MultipleCertificate multipleCertificate = new MultipleCertificate(certArrayOutputStreamList,this);
            multipleCertificate.setRecipient(recipients.get(i));
            threads[i] = new Thread(multipleCertificate);
            threads[i].start();
        }

        for (int i =0;i< recipients.size();i++){
            try{
                threads[i].join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(zipStream)) {

            for (Long id : certArrayOutputStreamList.keySet()) {
                ZipEntry zipEntry = new ZipEntry(id+ ".pdf");
                zipOut.putNextEntry(zipEntry);
                zipOut.write(certArrayOutputStreamList.get(id).toByteArray());
                zipOut.closeEntry();
            }

        } catch (IOException e) {
            throw new UploadException(e.getMessage());
        }

        return zipStream;
    }

    public ByteArrayOutputStream generateExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("id");
        headerRow.createCell(1).setCellValue("name");
        headerRow.createCell(2).setCellValue("date issued");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }

    public Recipient getRecipient(Long id) {
        if(this.recipients == null || this.recipients.isEmpty()){
            return null;
        }

        for (Recipient r : recipients) {
            if (r.getId().equals(id)) {
                return r;
            }
        }

        return null;
    }

}
