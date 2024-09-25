package com.colak.springtutorial.download.controller.memory.dynamicresource.image;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/bytearrayimage")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // http://localhost:8080/api/v1/bytearrayimage/download?filePath=download.jpg
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadImage(@RequestParam("filePath") String filePath) {
        try {
            byte[] imageWithWatermark = imageService.addWatermark(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=watermarked_image.jpg");
            return new ResponseEntity<>(imageWithWatermark, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

