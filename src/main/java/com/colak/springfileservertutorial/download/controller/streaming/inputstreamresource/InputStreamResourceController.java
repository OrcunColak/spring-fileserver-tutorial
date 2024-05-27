package com.colak.springfileservertutorial.download.controller.streaming.inputstreamresource;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/inputstreamsource")

@RequiredArgsConstructor
public class InputStreamResourceController {

    private final ResourceLoader resourceLoader;

    @GetMapping(value = "/download/{filename}")
    public ResponseEntity<?> download(@PathVariable("filename") String filename) throws IOException {

        String filePath = "classpath:" + filename;
        File file = ResourceUtils.getFile(filePath);

        // Read file size
        long fileSizeInBytes = file.length();
        // Create an input stream for the file
        FileInputStream fileInputStream = new FileInputStream(file);

        // Create a spring boot input stream wrapper
        InputStreamResource inputStreamResource = new InputStreamResource(fileInputStream);

        // return the input stream a response
        return ResponseEntity.ok()
                .contentLength(fileSizeInBytes)
                // APPLICATION_OCTET_STREAM shows that this is a stream
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .body(inputStreamResource);
    }
}
