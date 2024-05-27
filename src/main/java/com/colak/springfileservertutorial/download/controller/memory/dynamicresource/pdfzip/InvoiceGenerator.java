package com.colak.springfileservertutorial.download.controller.memory.dynamicresource.pdfzip;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
class InvoiceGenerator {

    public static List<Invoice> generateRandomInvoices(int numberOfInvoices) {
        List<Invoice> invoices = new ArrayList<>();
        Faker faker = new Faker();

        for (int i = 0; i < numberOfInvoices; i++) {
            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber(faker.number().digits(8));
            invoice.setClientName(faker.company().name());
            invoice.setDate(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
            invoice.setDetails(generateRandomDetails(faker));
            invoice.setTitleImg("path/to/title/image.png");

            invoices.add(invoice);
        }

        return invoices;
    }

    private static List<String> generateRandomDetails(Faker faker) {
        List<String> details = new ArrayList<>();
        int numberOfDetails = faker.number().numberBetween(1, 5);

        for (int i = 0; i < numberOfDetails; i++) {
            details.add(faker.lorem().sentence());
        }

        return details;
    }
}
