package com.dataart.playme.dto;

import com.dataart.playme.util.Constants;
import com.dataart.playme.validation.DateFrom;
import com.dataart.playme.validation.DateTo;
import com.dataart.playme.validation.Unique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;

    @NotNull(message = "no_login")
    @Size(min = Constants.Constraints.MIN_LOGIN_LENGTH, message = "short_login")
    @Size(max = Constants.Constraints.MAX_LOGIN_LENGTH, message = "long_login")
    @Pattern(regexp = Constants.Constraints.LOGIN_PATTERN, message = "wrong_login")
    @Unique
    private String login;

    @NotNull(message = "no_password")
    @Size(min = Constants.Constraints.MIN_PASSWORD_LENGTH, message = "short_password")
    @Size(max = Constants.Constraints.MAX_PASSWORD_LENGTH, message = "long_password")
    private String password;

    @NotNull(message = "no_email")
    @Size(max = Constants.Constraints.MAX_EMAIL_LENGTH, message = "long_email")
    @Pattern(regexp = Constants.Constraints.EMAIL_PATTERN, message = "email_format")
    @Unique
    private String email;

    @NotNull(message = "no_birthdate")
    @DateFrom(from = Constants.Constraints.MIN_BIRTHDATE, message = "early_birthdate")
    @DateTo(to = Constants.Constraints.MAX_BIRTHDATE, message = "late_birthdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    private Date creationDate;

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

    private String role;

    private String status;
}
