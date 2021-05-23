package com.dataart.playme.dto.cabinet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnterMessage {

    private String musicianId;

    private String rehearsalId;
}
