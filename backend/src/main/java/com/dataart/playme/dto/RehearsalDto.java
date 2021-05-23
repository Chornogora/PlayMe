package com.dataart.playme.dto;

import com.dataart.playme.validation.ValidRehearsal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@ValidRehearsal
public abstract class RehearsalDto {

    private Date startDatetime;

    private Date finishDatetime;

    private String description;

    private List<String> membersId;
}
