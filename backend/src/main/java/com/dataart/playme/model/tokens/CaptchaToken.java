package com.dataart.playme.model.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "captcha_tokens")
public class CaptchaToken extends Token {

    public CaptchaToken(String id, String content, Date creationDatetime) {
        super(id, content, creationDatetime);
    }
}
