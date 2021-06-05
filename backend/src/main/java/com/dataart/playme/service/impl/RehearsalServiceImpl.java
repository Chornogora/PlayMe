package com.dataart.playme.service.impl;

import com.dataart.playme.dto.CreateRehearsalDto;
import com.dataart.playme.dto.UpdateRehearsalDto;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Metronome;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Rehearsal;
import com.dataart.playme.repository.MetronomeRepository;
import com.dataart.playme.repository.RehearsalRepository;
import com.dataart.playme.service.CabinetService;
import com.dataart.playme.service.FileService;
import com.dataart.playme.service.NotificationService;
import com.dataart.playme.service.RehearsalService;
import com.dataart.playme.service.dto.RehearsalDtoTransformationService;
import com.dataart.playme.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RehearsalServiceImpl implements RehearsalService {

    private final CabinetService cabinetService;

    private final FileService fileService;

    private final RehearsalDtoTransformationService rehearsalDtoTransformationService;

    private final RehearsalRepository rehearsalRepository;

    private final MetronomeRepository metronomeRepository;

    private final NotificationService notificationService;

    @Autowired
    public RehearsalServiceImpl(CabinetService cabinetService, FileService fileService, RehearsalRepository rehearsalRepository,
                                MetronomeRepository metronomeRepository,
                                RehearsalDtoTransformationService rehearsalDtoTransformationService,
                                NotificationService notificationService) {
        this.cabinetService = cabinetService;
        this.fileService = fileService;
        this.rehearsalRepository = rehearsalRepository;
        this.metronomeRepository = metronomeRepository;
        this.rehearsalDtoTransformationService = rehearsalDtoTransformationService;
        this.notificationService = notificationService;
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
        Rehearsal savedRehearsal = rehearsalRepository.save(rehearsal);

        String messageText = createMessageText(dto);
        savedRehearsal.getMembers()
                .forEach(member -> notificationService.createNotification(member, messageText, "New rehearsal"));

        return savedRehearsal;
    }

    private String createMessageText(CreateRehearsalDto dto) {
        return String.format("%s created a rehearsal from %2$tH:%2$tM %2$te.%2$tm.%2$tY to " +
                "%3$tH:%3$tM %3$te.%3$tm.%3$tY and invited you to attend it",
                dto.getCreator().getUser().getLogin(), dto.getStartDatetime(), dto.getFinishDatetime());
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
        if (rehearsal.getRecord() != null && rehearsal.getRecord().getTracks() != null) {
            rehearsal.getRecord().getTracks().forEach(track -> {
                String trackFolder = Constants.get(Constants.TRACK_ROOT_DIRECTORY_ID);
                fileService.deleteFile(track.getFileUrl(), trackFolder);
            });
        }
        rehearsalRepository.delete(rehearsal);
    }

    @Override
    public Rehearsal addMetronome(String rehearsalId, int tempo) {
        Rehearsal rehearsal = rehearsalRepository.findById(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find rehearsal by id"));
        String metronomeId = UUID.randomUUID().toString();
        Metronome metronome = new Metronome(metronomeId, tempo,
                new Date(System.currentTimeMillis()), rehearsal);
        metronomeRepository.save(metronome);
        this.cabinetService.updateRehearsal(rehearsalId);
        return rehearsal;
    }

    @Override
    public Rehearsal deleteMetronome(String metronomeId) {
        Metronome metronome = metronomeRepository.findById(metronomeId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find metronome by id"));
        metronomeRepository.delete(metronome);
        this.cabinetService.updateRehearsal(metronome.getRehearsal().getId());
        return metronome.getRehearsal();
    }

    @Override
    public Rehearsal updateMetronome(String metronomeId, int tempo) {
        Metronome metronome = metronomeRepository.findById(metronomeId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find metronome by id"));
        metronome.setTempo(tempo);
        metronomeRepository.save(metronome);
        this.cabinetService.updateRehearsal(metronome.getRehearsal().getId());
        return metronome.getRehearsal();
    }
}
