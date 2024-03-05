package com.colak.springfileservertutorial.controller.memory.pdfzip;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
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
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);

                final int index = i;
                executorService.execute(() -> {
                    try {
                        byte[] pdfContent = generatePDF(invoice);
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
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = invoices.get(i);
                byte[] pdfContent = generatePDF(invoice);
                zipOutputStream.putNextEntry(new ZipEntry("invoice_" + (i + 1) + ".pdf"));
                zipOutputStream.write(pdfContent);
                zipOutputStream.closeEntry();
            }
        }
        return zipStream.toByteArray();
    }

    private byte[] generatePDF(Invoice invoice) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {
            addTitleText(document, "Dynamic title invoice 2024");
            // addTitleImage(document, invoice.getTitleImg(), false);
            addInvoiceTable(document, invoice);
        }
        return outputStream.toByteArray();
    }

    private void addTitleImage(Document document, String titleImage, boolean isExternalImg) {
        Image image = null;
        try {
            if (!isExternalImg) {
                image = new Image(ImageDataFactory.create(titleImage));
            } else {
                image = new Image(ImageDataFactory.create(new URL(titleImage)));
            }
            image.setWidth(100);
            image.setTextAlignment(TextAlignment.LEFT);
            document.add(image);
        } catch (Exception exception) {
            log.error("Error to get img", exception);
        }
    }

    // Title above the table
    private void addTitleText(Document document, String titleText) {
        Paragraph paragraph = new Paragraph(titleText).setBold().setFontSize(20).setMarginTop(20);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);
    }

    private void addInvoiceTable(Document document, Invoice invoice) {
        Table table = new Table(2);
        // Row
        table.addCell("Invoice Number");
        table.addCell(invoice.getInvoiceNumber());

        // Row
        table.addCell("Client Name");
        table.addCell(invoice.getClientName());

        // Row
        table.addCell("Date");
        table.addCell(invoice.getDate().toString());

        // Row
        table.addCell("Details");
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
        invoice.getDetails().forEach(list::add);
        table.addCell(list);

        document.add(table);
    }

}
