package com.dataart.playme.controller.socket;

import com.dataart.playme.dto.RecordChunkDto;
import com.dataart.playme.service.RecordService;
import com.dataart.playme.util.MonitorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class RecordController {

    private final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @MessageMapping("/record")
    public void enterCabinet(@Payload String recordEncoded,
                             @Header(name = "firstRecord") String firstRecordHeader,
                             @Header(name = "bitsPerSecond") String bitsPerSecondHeader,
                             @Header(name = "lastRecord") String lastRecordHeader,
                             @Header(name = "delayTime") int delayTime,
                             @Header(name = "filterNoise") boolean filterNoise,
                             SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        RecordChunkDto dto = new RecordChunkDto();
        dto.setChunk(recordEncoded);
        dto.setFirstRecord(Boolean.parseBoolean(firstRecordHeader));
        dto.setBitsPerSecond(Integer.parseInt(bitsPerSecondHeader));
        dto.setLastRecord(Boolean.parseBoolean(lastRecordHeader));
        dto.setDelayTime(delayTime);
        dto.setFilterNoise(filterNoise);
        recordService.saveChunk(sessionId, dto);
    }

    @MessageExceptionHandler(value = {IllegalStateException.class})
    public void handleException(IllegalStateException exception) {
        LOGGER.error(exception.getMessage());
    }
}
