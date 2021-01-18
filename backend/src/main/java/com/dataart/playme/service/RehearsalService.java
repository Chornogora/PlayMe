package com.dataart.playme.service;

import com.dataart.playme.dto.CreateRehearsalDto;
import com.dataart.playme.dto.UpdateRehearsalDto;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Rehearsal;

import java.util.List;

public interface RehearsalService {

    Rehearsal getRehearsal(String id);

    List<Rehearsal> getByMusician(Musician musician);

    Rehearsal createRehearsal(CreateRehearsalDto dto);

    Rehearsal updateRehearsal(UpdateRehearsalDto dto, Musician updateAuthor);

    void deleteRehearsal(String id, Musician deleteInitiator);
}
