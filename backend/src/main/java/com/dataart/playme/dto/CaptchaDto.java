package com.dataart.playme.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CaptchaDto {

    String tokenId;

    String image;
}
