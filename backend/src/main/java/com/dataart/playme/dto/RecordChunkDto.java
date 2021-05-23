package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordChunkDto {

    String chunk;

    boolean firstRecord;

    boolean lastRecord;

    int bitsPerSecond;
}
