package com.colak.springfileservertutorial.controller.memory;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
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
@RequestMapping("api/v1/bytearrayexcel")
public class ByteArrayExcelController {

    // http://localhost:8080/api/v1/excel/download
    // Downloads a sample.xlsx file
    @GetMapping("/download")
    public ResponseEntity<byte[]> download() throws IOException {
        ByteArrayOutputStream stream = generateExcel();

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        // File name
        String filename = "sample.xlsx";
        ContentDisposition contentDisposition = ContentDisposition
                .builder("attachment")
                .filename(filename)
                .build();
        headers.setContentDisposition(contentDisposition);
        // File type
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream.toByteArray());
    }

    private ByteArrayOutputStream generateExcel() throws IOException {
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
        return stream;
    }
}
