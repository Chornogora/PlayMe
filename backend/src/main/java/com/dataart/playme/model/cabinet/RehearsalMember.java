package com.dataart.playme.model.cabinet;

import com.dataart.playme.model.Musician;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RehearsalMember {

    private String sessionId;

    private Musician musician;

    private boolean microphoneEnabled;

    public String toString() {
        return "[" + getClass().getName() + "]";
    }
}
