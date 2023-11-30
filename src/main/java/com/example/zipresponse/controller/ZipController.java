package com.example.zipresponse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class ZipController {

    @GetMapping("/download-json-list-zip")
    public ResponseEntity<byte[]> downloadJsonListZip() throws IOException {
        // Create a list of objects (for example, strings)
        List<String> dataList = Arrays.asList("Item 1", "Item 2", "Item 3");

        // Convert the list to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(dataList);

        // Create a ByteArrayOutputStream to hold the zip content
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            // Add a JSON file to the zip
            ZipEntry entry = new ZipEntry("data.json");
            zos.putNextEntry(entry);
            zos.write(jsonData.getBytes());
            zos.closeEntry();
        }

        // Set up HTTP headers then browser will download automatically
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "data.zip");

        // Return the zip file as a byte array in the response body
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
}
