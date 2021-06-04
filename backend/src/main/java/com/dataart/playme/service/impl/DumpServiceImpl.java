package com.dataart.playme.service.impl;

import com.dataart.playme.service.DumpService;
import com.dataart.playme.util.Constants;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DumpServiceImpl implements DumpService {

    @Override
    public byte[] getDump() throws IOException {
        File file = new File(Constants.BACKUP_FILE_LOCATION + getDatetimeString());
        for(int i = 0; file.exists(); ++i){
            file = new File(Constants.BACKUP_FILE_LOCATION + getDatetimeString() + i);
        }
        String filePath = file.getAbsolutePath();

        String[] commands = {"pg_dump", "-U", "postgres", "-f", filePath, "playme"};
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.inheritIO();
        final Process process = builder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Files.readAllBytes(Paths.get(filePath));
    }

    private String getDatetimeString(){
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        return simpleDateFormat.format(currentDate);
    }
}
