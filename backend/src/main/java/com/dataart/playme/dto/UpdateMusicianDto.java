package com.dataart.playme.dto;

import com.dataart.playme.model.MusicianSkill;
import com.dataart.playme.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateMusicianDto {

    @NotNull(message = "no_email")
    @Size(max = Constants.Constraints.MAX_EMAIL_LENGTH, message = "long_email")
    @Pattern(regexp = Constants.Constraints.EMAIL_PATTERN, message = "email_format")
    private String email;

    @NotNull(message = "no_name")
    @Size(min = Constants.Constraints.MIN_NAME_LENGTH, message = "short_name")
    @Size(max = Constants.Constraints.MAX_NAME_LENGTH, message = "long_name")
    @Pattern(regexp = Constants.Constraints.NAME_PATTERN, message = "wrong_name")
    private String firstName;

    @NotNull(message = "no_surname")
    @Size(min = Constants.Constraints.MIN_NAME_LENGTH, message = "short_surname")
    @Size(max = Constants.Constraints.MAX_NAME_LENGTH, message = "long_surname")
    @Pattern(regexp = Constants.Constraints.NAME_PATTERN, message = "wrong_surname")
    private String lastName;

    private List<MusicianSkill> musicianSkills;

    private boolean emailNotifications;
}
