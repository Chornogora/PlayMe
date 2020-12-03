package com.dataart.playme.security.jwt;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JwtProperties {

    private String secretKey = "secret";

    //validity in milliseconds
    private long validityInMs = 1000*60*15; // 15 min
}

