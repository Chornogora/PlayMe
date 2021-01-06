package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileCreationDto {

    String fileContent;

    String fileName;

    String fileUrl;
}
