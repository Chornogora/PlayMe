package com.dataart.playme.dto;

import com.dataart.playme.model.Band;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostRequestDto {

    private static final int DEFAULT_LIMIT = 10;

    private static final int DEFAULT_OFFSET = 0;

    private List<Band> bands;

    private int limit = DEFAULT_LIMIT;

    private int offset = DEFAULT_OFFSET;
}
