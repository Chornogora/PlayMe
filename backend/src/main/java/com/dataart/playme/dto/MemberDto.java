package com.dataart.playme.dto;

import com.dataart.playme.model.Band;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
public class MemberDto {

    @Null(message = "cant_inject_band")
    private Band band;

    @NotNull
    private String musicianId;

    @NotNull
    private String statusName;
}
