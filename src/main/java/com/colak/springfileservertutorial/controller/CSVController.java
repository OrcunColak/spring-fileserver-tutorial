package com.colak.springfileservertutorial.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/csv")
public class CSVController {

    private final ResourceLoader resourceLoader;

    // http://localhost:8080/api/v1/csv/download-csv
    @GetMapping(value = "download-csv")
    ResponseEntity<Resource> downloadCsv() {
        // Downloads a songs.csv file
        HttpHeaders headers = new HttpHeaders();
        // File name
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=songs.csv");
        // File type
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        Resource fileResource = resourceLoader.getResource("classpath:songs.csv");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(fileResource);
    }

    // http://localhost:8080/api/v1/csv/download-excel
    @GetMapping("/download-excel")
    public ResponseEntity<byte[]> downloadExcel() throws IOException {
        // Downloads a sample.xlsx file
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Create a new Excel workbook and sheet
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("SampleSheet");

            // Create sample data (you can replace this with your own data)
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Name");
            row.createCell(1).setCellValue("Age");

            row = sheet.createRow(1);
            row.createCell(0).setCellValue("Meduim");
            row.createCell(1).setCellValue(30);

            // Write the workbook to a ByteArrayOutputStream
            workbook.write(stream);
        }

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        // File name
        headers.setContentDispositionFormData("attachment", "sample.xlsx");
        // File type
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream.toByteArray());
    }
}
