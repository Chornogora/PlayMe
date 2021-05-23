package com.dataart.playme.service;

import com.dataart.playme.dto.RecordChunkDto;
import com.dataart.playme.model.Track;

public interface RecordService {

    Track findTrackById(String trackId);

    void saveChunk(String sessionId, RecordChunkDto dto);
}
