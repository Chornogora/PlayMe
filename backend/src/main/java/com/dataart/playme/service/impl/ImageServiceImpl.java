package com.dataart.playme.service.impl;

import com.dataart.playme.exception.ApplicationRuntimeException;
import com.dataart.playme.service.FileService;
import com.dataart.playme.service.ImageService;
import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImageServiceImpl implements ImageService {

    private final FileService fileService;

    @Autowired
    public ImageServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public String createImage(String imageEncoded, String fileName) {
        try {
            byte[] data = Base64.getDecoder().decode(imageEncoded.split(",")[1]);
            String imageDirectory = Constants.get(Constants.IMAGE_ROOT_DIRECTORY_ID);
            return fileService.writeToFile(imageDirectory, fileName, data);
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Cannot write image to file");
        }
    }
}
