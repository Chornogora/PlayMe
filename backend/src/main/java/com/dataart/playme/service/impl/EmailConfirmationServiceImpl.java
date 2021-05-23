package com.dataart.playme.service.impl;

import com.dataart.playme.model.Role;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.EmailConfirmationTokenRepository;
import com.dataart.playme.repository.StatusRepository;
import com.dataart.playme.repository.UserRepository;
import com.dataart.playme.security.Encoder;
import com.dataart.playme.service.EmailConfirmationService;
import com.dataart.playme.service.MusicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private static final Duration EMAIL_CONFIRMATION_TOKEN_LIFETIME = Duration.ofDays(1);

    private static final long CLEAN_EMAIL_CONFIRMATION_TOKENS_PERIOD = TimeUnit.DAYS.toMillis(1);

    private final Encoder encoder;

    private final MusicianService musicianService;

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    private final StatusRepository statusRepository;

    private final UserRepository userRepository;

    @Autowired
    public EmailConfirmationServiceImpl(Encoder encoder, MusicianService musicianService, EmailConfirmationTokenRepository emailConfirmationTokenRepository,
                                        StatusRepository statusRepository, UserRepository userRepository) {
        this.encoder = encoder;
        this.musicianService = musicianService;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void confirmEmail(String token) {
        String content = encoder.encode(token);
        User user = emailConfirmationTokenRepository.findByContent(content)
                .orElseThrow(() -> new NoSuchElementException("Token was not found"))
                .getUser();
        if (user.getStatus().getName().equals(Status.StatusName.PENDING.getValue())) {
            Status activeStatus = statusRepository.findByName(Status.StatusName.ACTIVE.getValue())
                    .orElseThrow(() -> new NoSuchElementException("Cannot find active status"));
            activateUser(user, activeStatus);
        }
    }

    @Transactional
    protected void activateUser(User user, Status activeStatus) {
        user.setStatus(activeStatus);
        User updated = userRepository.save(user);
        if (user.getRole().getName().equals(Role.RoleName.USER.getValue())) {
            musicianService.createMusician(updated);
        }
    }

    @Autowired
    private void startCleanTokensTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        new Timer().schedule(new CleanEmailConfirmationTokensTimerTask(),
                calendar.getTime(), CLEAN_EMAIL_CONFIRMATION_TOKENS_PERIOD);
    }

    private class CleanEmailConfirmationTokensTimerTask extends TimerTask {

        @Override
        public void run() {
            Date minimalDate = getMinimalDate();
            emailConfirmationTokenRepository.cleanTokens(minimalDate);
        }

        private Date getMinimalDate() {
            Instant now = Instant.now();
            Instant before = now.minus(EMAIL_CONFIRMATION_TOKEN_LIFETIME);
            return Date.from(before);
        }
    }
}
