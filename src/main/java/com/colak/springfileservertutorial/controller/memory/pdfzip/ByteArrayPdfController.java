package com.colak.springfileservertutorial.controller.memory.pdfzip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/bytearraypdf")

@RequiredArgsConstructor
@Slf4j
public class ByteArrayPdfController {


    // http://localhost:8080/api/v1/bytearraypdf/download
    @GetMapping("/download")
    public ResponseEntity<byte[]> download() {
        try {
            List<Invoice> invoices = InvoiceGenerator.generateRandomInvoices(10);

            PDFGenerator pdfGenerator = new PDFGenerator();
            byte[] zipContent = pdfGenerator.generatePDFs(invoices);
            return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=invoices.zip")
                    .body(zipContent);
        } catch (IOException exception) {
            log.error("Exception : ", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
