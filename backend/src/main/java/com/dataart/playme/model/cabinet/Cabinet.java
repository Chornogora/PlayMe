package com.dataart.playme.model.cabinet;

import com.dataart.playme.model.Rehearsal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Cabinet {

    private Rehearsal rehearsal;

    private List<RehearsalMember> members;

    private RehearsalState rehearsalState = RehearsalState.STOPPED;
}
