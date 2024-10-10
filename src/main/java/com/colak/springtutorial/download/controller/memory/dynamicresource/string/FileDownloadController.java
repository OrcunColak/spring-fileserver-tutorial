package com.colak.springtutorial.download.controller.memory.dynamicresource.string;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("api/v1/bytearraystring")
public class FileDownloadController {

    // http://localhost:8080/api/v1/bytearraystring/download
    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> downloadFile() {
        String fileContent = "This is a downloadable file content.";
        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.txt\"");

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }
}

