package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateCommentDto {

    @NotNull
    @NotBlank
    private String text;
}
