package com.dataart.playme.service.impl;

import com.dataart.playme.dto.RecordChunkDto;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Record;
import com.dataart.playme.model.Track;
import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.cabinet.RehearsalState;
import com.dataart.playme.repository.CabinetRepository;
import com.dataart.playme.repository.TrackRepository;
import com.dataart.playme.service.RecordService;
import com.dataart.playme.util.Constants;
import org.postgresql.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecordServiceImpl implements RecordService {

    private final Logger LOGGER = LoggerFactory.getLogger(RecordServiceImpl.class);

    private static final String TRACK_MARK = "track_";

    private static final String EXTENSION = ".webm";

    private final CabinetRepository cabinetRepository;

    private final TrackRepository trackRepository;

    @Autowired
    public RecordServiceImpl(CabinetRepository cabinetRepository,
                             TrackRepository trackRepository) {
        this.cabinetRepository = cabinetRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    public Track findTrackById(String trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find track by id"));
    }

    @Override
    public void saveChunk(String sessionId, RecordChunkDto dto) {
        Track track = prepare(sessionId, dto.isLastRecord());
        byte[] decoded = decodeRecord(dto.getChunk());

        String rootDirectory = Constants.get(Constants.FILE_STORAGE_PATH_ID);
        String trackDirectory = Constants.get(Constants.TRACK_ROOT_DIRECTORY_ID);
        String filePath = rootDirectory + trackDirectory + "\\" +
                track.getFileUrl();

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            writeToFile(decoded, file, dto);
        } catch (IOException e) {
            LOGGER.error("Can't write to file", e);
        }
    }

    private byte[] decodeRecord(String encodedRecord) {
        String lineToDecode = encodedRecord.split(",")[1];
        return Base64.decode(lineToDecode);
    }

    private Track prepare(String sessionId, boolean lastRecord) {
        Cabinet cabinet = cabinetRepository.findBySessionId(sessionId);
        if (cabinet.getRehearsalState() == RehearsalState.STARTED || lastRecord) {
            Record record = cabinet.getRehearsal().getRecord();
            return getTrack(sessionId, cabinet, record);
        } else {
            throw new IllegalStateException("Rehearsal is not started");
        }
    }

    private Track getTrack(String sessionId, Cabinet cabinet, Record record) {
        Musician player = findPlayer(cabinet, sessionId);
        return record.getTracks().stream()
                .filter(track -> track.getMusician().getId().equals(player.getId()))
                .findFirst()
                .or(() -> Optional.of(createTrack(player, record)))
                .get();
    }

    private Track createTrack(Musician player, Record record) {
        File file = createFile();
        Track track = new Track();
        track.setId(UUID.randomUUID().toString());
        track.setRecord(record);
        track.setFileUrl(file.getName());
        track.setMusician(player);
        Track created = trackRepository.save(track);
        record.getTracks().add(created);
        return created;
    }

    private File createFile() {
        String rootDirectory = Constants.get(Constants.FILE_STORAGE_PATH_ID);
        String trackDirectory = Constants.get(Constants.TRACK_ROOT_DIRECTORY_ID);
        String fileId = UUID.randomUUID().toString();
        return new File(rootDirectory + trackDirectory,
                TRACK_MARK + fileId + EXTENSION);
    }

    private Musician findPlayer(Cabinet cabinet, String sessionId) {
        return cabinet.getMembers().stream()
                .filter(member -> sessionId.equals(member.getSessionId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchRecordException("Can't find musician by sessionId"))
                .getMusician();
    }

    private void writeToFile(byte[] content, File file, RecordChunkDto dto) throws IOException {
        StandardOpenOption option;
        if (dto.isFirstRecord()) {
            option = StandardOpenOption.TRUNCATE_EXISTING;
        } else {
            option = StandardOpenOption.APPEND;
        }
        Files.write(file.toPath(), content, option);
    }
}
