package com.dataart.playme.controller.rest;

import com.dataart.playme.model.Track;
import com.dataart.playme.service.FileService;
import com.dataart.playme.service.RecordService;
import com.dataart.playme.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final Logger LOGGER = LoggerFactory.getLogger(TrackController.class);

    private final FileService fileService;

    private final RecordService recordService;

    @Autowired
    public TrackController(FileService fileService, RecordService recordService) {
        this.fileService = fileService;
        this.recordService = recordService;
    }

    @GetMapping(value = "/{trackId}", produces = "audio/webm")
    public ResponseEntity<Resource> getTrack(@PathVariable String trackId) {
        String trackFolder = Constants.get(Constants.TRACK_ROOT_DIRECTORY_ID);
        Track track = recordService.findTrackById(trackId);

        try {
            byte[] bytes = fileService.readFromFile(track.getFileUrl(), trackFolder);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

            return ResponseEntity.ok()
                    .contentLength(bytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            LOGGER.error("Can't read track", e);
            throw new UncheckedIOException(e);
        }
    }
}
