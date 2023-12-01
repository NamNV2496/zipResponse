package com.example.zipresponse.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/zipJson")
    public ResponseEntity<byte[]> zipJsonFile() throws IOException {

        // Read JSON file from resources
        ClassPathResource jsonResource = new ClassPathResource("large-file.json");

//        String zipFilePath = "path/to/your/destination/file.zip";
//        FileOutputStream fos = new FileOutputStream(zipFilePath);
//        ZipOutputStream zipOut = new ZipOutputStream(fos);

        InputStream jsonStream = jsonResource.getInputStream();
        byte[] jsonData = jsonStream.readAllBytes();

        // Create a ByteArrayOutputStream to hold the zipped content
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
            // Add JSON file to the zip
            ZipEntry entry = new ZipEntry("data.json");
            zipOut.putNextEntry(entry);
            zipOut.write(jsonData);
            zipOut.closeEntry();
        }

        // Set up response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "data.zip");

        // Return the zipped content as a byte array
        return ResponseEntity.ok()
            .headers(headers)
            .body(baos.toByteArray());
    }
}

