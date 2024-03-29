package com.colak.springfileservertutorial.controller.memory.pdfzip;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
class Invoice {
    private String invoiceNumber;
    private String clientName;
    private LocalDate date;
    private List<String> details;
    private String titleImg;
}
