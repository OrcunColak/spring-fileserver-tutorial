package com.colak.springfileservertutorial.download.controller.memory.pdfzip;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
class PDFGenerator {

    private final ExecutorService executorService;

    public PDFGenerator() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    // for best performance
    public byte[] generatePDFsIn(List<Invoice> invoices) throws IOException, InterruptedException {
        ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
        InvoicePDFGenerator invoicePDFGenerator = new InvoicePDFGenerator();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);

                final int index = i;
                executorService.execute(() -> {
                    try {
                        byte[] pdfContent = invoicePDFGenerator.generatePDF(invoice);
                        synchronized (zipOutputStream) {
                            zipOutputStream.putNextEntry(new ZipEntry("invoice_" + (index + 1) + ".pdf"));
                            zipOutputStream.write(pdfContent);
                            zipOutputStream.closeEntry();
                        }
                    } catch (IOException exception) {
                        log.error("Error to get img", exception);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        return zipStream.toByteArray();
    }

    public byte[] generatePDFs(List<Invoice> invoices) throws IOException {
        ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
        InvoicePDFGenerator invoicePDFGenerator = new InvoicePDFGenerator();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);
                byte[] pdfContent = invoicePDFGenerator.generatePDF(invoice);
                zipOutputStream.putNextEntry(new ZipEntry("invoice_" + (i + 1) + ".pdf"));
                zipOutputStream.write(pdfContent);
                zipOutputStream.closeEntry();
            }
        }
        return zipStream.toByteArray();
    }



}
