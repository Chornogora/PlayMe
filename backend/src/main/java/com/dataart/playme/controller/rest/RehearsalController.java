package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.dto.CreateRehearsalDto;
import com.dataart.playme.dto.UpdateRehearsalDto;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Rehearsal;
import com.dataart.playme.service.RehearsalService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rehearsals")
public class RehearsalController {

    private final RehearsalService rehearsalService;

    public RehearsalController(RehearsalService rehearsalService) {
        this.rehearsalService = rehearsalService;
    }

    @GetMapping("/{id}")
    public Rehearsal getRehearsal(@PathVariable String id) {
        return rehearsalService.getRehearsal(id);
    }

    @GetMapping
    public List<Rehearsal> getMusicianRehearsals(@CurrentMusician Musician currentMusician) {
        return rehearsalService.getByMusician(currentMusician);
    }

    @PostMapping
    public Rehearsal createRehearsal(@CurrentMusician Musician currentMusician,
                                     @Valid @RequestBody CreateRehearsalDto dto) {
        dto.setCreator(currentMusician);
        return rehearsalService.createRehearsal(dto);
    }

    @PutMapping
    public Rehearsal updateRehearsal(@Valid @RequestBody UpdateRehearsalDto dto,
                                     @CurrentMusician Musician currentMusician) {
        return rehearsalService.updateRehearsal(dto, currentMusician);
    }

    @DeleteMapping("/{id}")
    public void deleteRehearsal(@PathVariable String id,
                                @CurrentMusician Musician currentMusician) {
        rehearsalService.deleteRehearsal(id, currentMusician);
    }
}
