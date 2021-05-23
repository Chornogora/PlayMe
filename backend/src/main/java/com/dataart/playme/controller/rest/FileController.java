package com.dataart.playme.controller.rest;

import com.dataart.playme.exception.ApplicationRuntimeException;
import com.dataart.playme.service.FileService;
import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.NotFoundException;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            String imageFolder = Constants.get(Constants.FILE_ROOT_DIRECTORY_ID);
            byte[] bytes = fileService.readFromFile(fileName, imageFolder);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

            return ResponseEntity.ok()
                    .contentLength(bytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("Cannot find file");
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Cannot read from file");
        }
    }
}
