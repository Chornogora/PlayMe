package com.dataart.playme.model.tokens;

import com.dataart.playme.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "email_confirmation_tokens")
public class EmailConfirmationToken extends Token {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
