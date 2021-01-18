package com.dataart.playme.dto;

import com.dataart.playme.model.Musician;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateRehearsalDto extends RehearsalDto {

    @Null
    private Musician creator;
}
