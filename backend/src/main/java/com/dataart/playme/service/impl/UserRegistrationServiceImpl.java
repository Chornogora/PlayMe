package com.dataart.playme.service.impl;

import com.dataart.playme.dto.UserDto;
import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.model.tokens.EmailConfirmationToken;
import com.dataart.playme.repository.EmailConfirmationTokenRepository;
import com.dataart.playme.security.Encoder;
import com.dataart.playme.service.MailService;
import com.dataart.playme.service.UserRegistrationService;
import com.dataart.playme.service.UserService;
import com.dataart.playme.service.dto.UserDtoTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final String EMAIL_CONFIRMATION_MESSAGE_SUBJECT = "Email confirmation";

    private static final String EMAIL_CONFIRMATION_MESSAGE_TEMPLATE_PATH = "./src/main/resources/mail_templates/email_confirmation.html";

    private static final String NAME_PARAMETER = "name";

    private static final String LINK_PARAMETER = "link";

    private static final String EMAIL_LINK_PATH = "http://localhost:8080/email/confirmation/";

    private final UserDtoTransformationService userDtoTransformationService;

    private final UserService userService;

    private final MailService mailService;

    private final Encoder encoder;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    @Autowired
    public UserRegistrationServiceImpl(UserDtoTransformationService userDtoTransformationService, UserService userService, MailService mailService, Encoder encoder, EmailConfirmationTokenRepository emailConfirmationTokenRepository) {
        this.userDtoTransformationService = userDtoTransformationService;
        this.userService = userService;
        this.mailService = mailService;
        this.encoder = encoder;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
    }

    @Override
    public User registerUser(UserDto dto) {
        dto.setRole(Role.RoleName.USER.getValue());
        dto.setStatus(Status.StatusName.PENDING.getValue());
        User user = userDtoTransformationService.dtoToUser(dto);
        return registerUser(user);
    }

    @Transactional
    public User registerUser(User user) {
        User createdUser = userService.addUser(user);

        String tokenValue = UUID.randomUUID().toString();
        EmailConfirmationToken token = createEmailConfirmationToken(createdUser, tokenValue);
        emailConfirmationTokenRepository.save(token);

        try {
            sendEmail(createdUser, tokenValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return createdUser;
    }

    private EmailConfirmationToken createEmailConfirmationToken(User user, String tokenValue) {
        EmailConfirmationToken token = new EmailConfirmationToken();
        token.setUser(user);
        token.setId(UUID.randomUUID().toString());
        token.setCreationDatetime(new Date(System.currentTimeMillis()));

        String tokenContent = encoder.encode(tokenValue);
        token.setToken(tokenContent);
        return token;
    }

    private void sendEmail(User createdUser, String tokenValue) throws IOException {
        Map<String, String> parameters = Map.of(NAME_PARAMETER, createdUser.getFirstName(),
                LINK_PARAMETER, EMAIL_LINK_PATH + tokenValue);
        mailService.sendThroughRemote(createdUser.getEmail(), EMAIL_CONFIRMATION_MESSAGE_SUBJECT,
                EMAIL_CONFIRMATION_MESSAGE_TEMPLATE_PATH, parameters);
    }
}
