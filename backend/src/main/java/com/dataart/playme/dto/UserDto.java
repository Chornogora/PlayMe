package com.dataart.playme.dto;

import com.dataart.playme.util.Constants;
import com.dataart.playme.validation.Unique;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends EditUserDto {

    private String id;

    @Unique
    private String email;

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

    private Date creationDate;

    private String role;

    public void setEmail(String email) {
        super.setEmail(email);
        this.email = email;
    }
}
