package com.dataart.playme.service.impl;

import com.dataart.playme.service.FileService;
import com.dataart.playme.util.Constants;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final String FILE_MARK = "file_";

    @Override
    public byte[] readFromFile(String fileName, String folderName) throws IOException {
        String filePath = Constants.get(Constants.FILE_STORAGE_PATH_ID) + folderName + "\\" + fileName;
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return fis.readAllBytes();
        }
    }

    @Override
    public String writeToFile(String folderPath, byte[] data) throws IOException {
        String resourceId = UUID.randomUUID().toString();
        String filePath = Constants.get(Constants.FILE_STORAGE_PATH_ID) +
                folderPath + "\\" + FILE_MARK + resourceId;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
        return FILE_MARK + resourceId;
    }
}
