package com.example.certificateGenerator.service;

import com.example.certificateGenerator.aspect.LoggingAspect;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


public interface FileService{

    public List<Recipient> processFileContent(MultipartFile file, Certificate certificate) throws IOException ;

    public ByteArrayOutputStream generateCertificatePdf(Long id);

    public ByteArrayOutputStream downloadCertificates();

    public ByteArrayOutputStream generateExcel() throws IOException;

    public Recipient getRecipient(Long id);

}
