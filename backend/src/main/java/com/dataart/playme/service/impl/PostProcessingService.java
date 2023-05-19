package com.dataart.playme.service.impl;

import com.dataart.playme.dto.RecordChunkDto;
import com.dataart.playme.util.MixingAudioInputStream;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class PostProcessingService {

    public void postProcess(String filePath, RecordChunkDto dto) throws IOException {
        try {
            new File(filePath).renameTo(new File(filePath + ".tmp"));
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", "\"" + filePath + ".tmp\"", "\"" + filePath + "\"");
            Process process = processBuilder.start();
            process.waitFor();
            new File(filePath + ".tmp").delete();

            if (dto.isFilterNoise()) {
                filterNoise(filePath, dto);
            }

            applyDelay(filePath, dto);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void applyDelay(String filePath, RecordChunkDto dto) throws IOException {

        if (dto.getDelayTime() == 0) {
            deleteOldFilesIfExist(filePath);
            return;
        }

        AudioFormat audioFormat = new AudioFormat(48000.0f, 16, 1, true, false);

        AudioInputStream aist1 = null;
        AudioInputStream aist2 = null;
        AudioInputStream silence = null;
        AudioInputStream diplicatedStream = null;
        MixingAudioInputStream mixer = null;
        try {
            File file = new File(filePath);
            aist1 = AudioSystem.getAudioInputStream(file.toURI().toURL());
            File file2 = new File(filePath);
            aist2 = AudioSystem.getAudioInputStream(file2.toURI().toURL());
            String silenceFilename = String.format("Silence-%d-%d.wav", (dto.getDelayTime()) / 4,
                    (dto.getDelayTime() % 4) * 25);
            File silenceFile = new File("src/main/resources/media/silence/" + silenceFilename);
            silence = AudioSystem.getAudioInputStream(silenceFile.toURI().toURL());

            diplicatedStream = new AudioInputStream(
                    new SequenceInputStream(silence, aist2), audioFormat,
                    silence.getFrameLength() + aist2.getFrameLength());
            File edited = new File(filePath.split("\\.wav")[0] + "-canon.wav");
            AudioSystem.write(diplicatedStream, AudioFileFormat.Type.WAVE, edited);

            diplicatedStream = AudioSystem.getAudioInputStream(edited.toURI().toURL());
            List<AudioInputStream> aists = Arrays.asList(diplicatedStream, aist1);

            mixer = new MixingAudioInputStream(audioFormat, aists);
            File canonFile = new File(filePath.split("\\.wav")[0] + "-mixed.wav");
            AudioSystem.write(mixer, AudioFileFormat.Type.WAVE, canonFile);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } finally {
            closeStream(aist1);
            closeStream(aist2);
            closeStream(silence);
            closeStream(diplicatedStream);
            closeStream(mixer);
        }
    }

    private void deleteOldFilesIfExist(String filePath) {
        File canonFile = new File(filePath.split("\\.wav")[0] + "-canon.wav");
        if (canonFile.exists()) {
            canonFile.delete();
        }

        File mixedFile = new File(filePath.split("\\.wav")[0] + "-mixed.wav");
        if (mixedFile.exists()) {
            mixedFile.delete();
        }
    }

    private void filterNoise(String filePath, RecordChunkDto dto) {
        System.out.println("Noise filtering enabled");
    }

    private void closeStream(InputStream s) {
        try {
            if (s != null) {
                s.close();
            }
        } catch (IOException e) {
            System.out.println("Can't close stream");
        }
    }
}
