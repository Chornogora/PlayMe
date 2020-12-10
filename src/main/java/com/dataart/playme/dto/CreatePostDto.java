package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatePostDto {

    private String photo;

    private String file;

    private String text;
}
