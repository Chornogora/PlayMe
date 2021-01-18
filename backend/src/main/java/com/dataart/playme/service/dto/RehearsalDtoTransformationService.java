package com.dataart.playme.service.dto;

import com.dataart.playme.dto.CreateRehearsalDto;
import com.dataart.playme.dto.UpdateRehearsalDto;
import com.dataart.playme.model.Rehearsal;

public interface RehearsalDtoTransformationService {

    Rehearsal creationDtoToRehearsal(CreateRehearsalDto dto);

    Rehearsal updateDtoToRehearsal(UpdateRehearsalDto dto);
}
