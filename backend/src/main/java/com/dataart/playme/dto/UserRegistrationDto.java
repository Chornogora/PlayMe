package com.dataart.playme.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRegistrationDto extends UserDto {

    private String captchaTokenId;

    private int captchaNumber;
}
