package com.dataart.playme.dto.cabinet;

import com.dataart.playme.model.cabinet.MetronomeConfiguration;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateMetronomeMessage {

    String rehearsalId;

    MetronomeConfiguration metronomeConfiguration;
}
