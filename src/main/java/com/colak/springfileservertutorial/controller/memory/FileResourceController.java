package com.colak.springfileservertutorial.controller.memory;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/file")

@RequiredArgsConstructor
public class FileResourceController {

    private final ResourceLoader resourceLoader;

    // http://localhost:8080/api/v1/file/download
    @GetMapping(value = "download")
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

}
