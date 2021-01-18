package com.dataart.playme.service.dto.impl;

import com.dataart.playme.dto.CreateRehearsalDto;
import com.dataart.playme.dto.RehearsalDto;
import com.dataart.playme.dto.UpdateRehearsalDto;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Rehearsal;
import com.dataart.playme.repository.MusicianRepository;
import com.dataart.playme.service.dto.RehearsalDtoTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RehearsalDtoTransformationServiceImpl implements RehearsalDtoTransformationService {

    private final MusicianRepository musicianRepository;

    @Autowired
    public RehearsalDtoTransformationServiceImpl(MusicianRepository musicianRepository) {
        this.musicianRepository = musicianRepository;
    }

    @Override
    public Rehearsal creationDtoToRehearsal(CreateRehearsalDto dto) {
        Rehearsal rehearsal = rehearsalDtoToRehearsal(dto);
        rehearsal.setCreator(dto.getCreator());
        return rehearsal;
    }

    @Override
    public Rehearsal updateDtoToRehearsal(UpdateRehearsalDto dto) {
        Rehearsal rehearsal = rehearsalDtoToRehearsal(dto);
        rehearsal.setId(dto.getId());
        return rehearsal;
    }

    private Rehearsal rehearsalDtoToRehearsal(RehearsalDto dto) {
        Rehearsal rehearsal = new Rehearsal();
        rehearsal.setDescription(dto.getDescription());
        rehearsal.setStartDatetime(dto.getStartDatetime());
        rehearsal.setFinishDatetime(dto.getFinishDatetime());

        List<Musician> members = musicianRepository.findAllById(dto.getMembersId());
        rehearsal.setMembers(members);

        return rehearsal;
    }
}
