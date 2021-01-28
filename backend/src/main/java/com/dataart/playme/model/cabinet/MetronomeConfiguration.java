package com.dataart.playme.model.cabinet;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class MetronomeConfiguration {

    private boolean enabled = false;

    private String metronomeId;
}
