package com.dataart.playme.service.impl;

import com.dataart.playme.dto.CreateRehearsalDto;
import com.dataart.playme.dto.UpdateRehearsalDto;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Rehearsal;
import com.dataart.playme.repository.RehearsalRepository;
import com.dataart.playme.service.RehearsalService;
import com.dataart.playme.service.dto.RehearsalDtoTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import java.util.List;
import java.util.UUID;

@Service
public class RehearsalServiceImpl implements RehearsalService {

    private final RehearsalRepository rehearsalRepository;

    private final RehearsalDtoTransformationService rehearsalDtoTransformationService;

    @Autowired
    public RehearsalServiceImpl(RehearsalRepository rehearsalRepository, RehearsalDtoTransformationService rehearsalDtoTransformationService) {
        this.rehearsalRepository = rehearsalRepository;
        this.rehearsalDtoTransformationService = rehearsalDtoTransformationService;
    }

    @Override
    public Rehearsal getRehearsal(String id) {
        return rehearsalRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordException("Cannot find rehearsal by id"));
    }

    @Override
    public List<Rehearsal> getByMusician(Musician musician) {
        return rehearsalRepository.findByMusician(musician);
    }

    @Override
    public Rehearsal createRehearsal(CreateRehearsalDto dto) {
        Rehearsal rehearsal = rehearsalDtoTransformationService.creationDtoToRehearsal(dto);
        String id = UUID.randomUUID().toString();
        rehearsal.setId(id);
        return rehearsalRepository.save(rehearsal);
    }

    @Override
    public Rehearsal updateRehearsal(UpdateRehearsalDto dto, Musician updateAuthor) {
        Rehearsal rehearsal = rehearsalDtoTransformationService.updateDtoToRehearsal(dto);
        Rehearsal oldValue = rehearsalRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchRecordException("Can't find rehearsal by id"));
        if (!updateAuthor.getId().equals(oldValue.getCreator().getId())) {
            throw new ForbiddenException("Only rehearsal creator can make changes");
        }
        rehearsal.setCreator(oldValue.getCreator());
        return rehearsalRepository.save(rehearsal);
    }

    @Override
    public void deleteRehearsal(String id, Musician deleteInitiator) {
        Rehearsal rehearsal = getRehearsal(id);
        if (!deleteInitiator.getId().equals(rehearsal.getCreator().getId())) {
            throw new ForbiddenException("Only rehearsal creator can make changes");
        }
        rehearsalRepository.delete(rehearsal);
    }
}
