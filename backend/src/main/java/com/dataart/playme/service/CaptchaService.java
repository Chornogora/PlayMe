package com.dataart.playme.service;

import com.dataart.playme.dto.CaptchaDto;

public interface CaptchaService {

    CaptchaDto generateCaptcha();

    boolean checkCaptcha(String tokenId, int number);
}
