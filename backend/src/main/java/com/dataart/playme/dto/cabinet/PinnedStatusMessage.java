package com.dataart.playme.dto.cabinet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PinnedStatusMessage {

    String musicianId;

    boolean pinnedStatus;
}
