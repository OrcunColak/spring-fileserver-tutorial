package com.colak.springfileservertutorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/csv")
public class CSVController {

    private final ResourceLoader resourceLoader;

    // http:localhost:8080/api/v1/csv/getSongs
    @GetMapping(value = "getSongs")
    ResponseEntity<Resource> getSongs() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=songs.csv");
        headers.setContentType(MediaType.parseMediaType("text/csv"));

        final Resource fileResource = resourceLoader.getResource("classpath:songs.csv");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(fileResource);
    }
}
