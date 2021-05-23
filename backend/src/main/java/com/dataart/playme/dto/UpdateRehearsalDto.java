package com.dataart.playme.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateRehearsalDto extends RehearsalDto {

    @NotBlank
    private String id;
}
