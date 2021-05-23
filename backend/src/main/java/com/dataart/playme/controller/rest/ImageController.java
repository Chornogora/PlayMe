package com.dataart.playme.controller.rest;

import com.dataart.playme.exception.ApplicationRuntimeException;
import com.dataart.playme.service.FileService;
import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.NotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final FileService fileService;

    @Autowired
    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/{imageFileName}",
            produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getImage(@PathVariable String imageFileName) {
        try {
            String imageFolder = Constants.get(Constants.IMAGE_ROOT_DIRECTORY_ID);
            return fileService.readFromFile(imageFileName, imageFolder);
        } catch (FileNotFoundException e) {
            throw new NotFoundException("Cannot find file");
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Cannot read from file");
        }
    }
}
