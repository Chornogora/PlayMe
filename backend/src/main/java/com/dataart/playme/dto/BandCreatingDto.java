package com.dataart.playme.dto;

import com.dataart.playme.model.Membership;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class BandCreatingDto {

    @NotNull(message = "no_name")
    @Size(min = 2, message = "short_name")
    @Size(max = 64, message = "long_name")
    private String name;

    @Null
    private List<Membership> members;

    private String logo;
}
