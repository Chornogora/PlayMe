package com.dataart.playme.service;

import java.io.IOException;

public interface FileService {

    byte[] readFromFile(String fileName, String folderName) throws IOException;

    String writeToFile(String folderPath, byte[] data) throws IOException;
}
