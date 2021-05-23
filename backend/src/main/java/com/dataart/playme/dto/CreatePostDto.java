package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreatePostDto {

    private String text;

    private List<FileCreationDto> files;

    private List<FileCreationDto> photos;
}
