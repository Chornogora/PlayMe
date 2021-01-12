package com.dataart.playme.service.impl;

import com.dataart.playme.dto.CaptchaDto;
import com.dataart.playme.model.tokens.CaptchaToken;
import com.dataart.playme.repository.CaptchaTokenRepository;
import com.dataart.playme.security.Encoder;
import com.dataart.playme.service.CaptchaService;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DummyWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    private static final Duration CAPTCHA_TOKEN_LIFETIME = Duration.ofDays(1);

    private static final long CLEAN_CAPTCHA_TOKENS_PERIOD = TimeUnit.DAYS.toMillis(1);

    private static final int MAX_NUMBER = 9999;

    private static final int MIN_NUMBER = 1000;

    private final SecureRandom random = new SecureRandom();

    private final Encoder encoder;

    private final CaptchaTokenRepository captchaTokenRepository;

    @Autowired
    public CaptchaServiceImpl(Encoder encoder, CaptchaTokenRepository captchaTokenRepository) {
        this.encoder = encoder;
        this.captchaTokenRepository = captchaTokenRepository;
    }

    @Override
    public CaptchaDto generateCaptcha() {
        CaptchaDto result = new CaptchaDto();
        int number = generateNumber();

        try {
            result.setImage(getCaptcha(number));
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e);
        }
        CaptchaToken captchaToken = saveToken(number);
        result.setTokenId(captchaToken.getId());
        return result;
    }

    @Override
    public boolean checkCaptcha(String tokenId, int number) {
        CaptchaToken token = captchaTokenRepository.findById(tokenId)
                .orElseThrow(() -> new NoSuchElementException("Token was not found"));
        String entered = encoder.encode(String.valueOf(number));
        return token.getToken().equals(entered);
    }

    private CaptchaToken saveToken(int number) {
        String id = UUID.randomUUID().toString();
        String tokenContent = encoder.encode(String.valueOf(number));
        Date now = new Date(System.currentTimeMillis());

        CaptchaToken captchaToken = new CaptchaToken(id, tokenContent, now);
        return captchaTokenRepository.save(captchaToken);
    }

    private int generateNumber() {
        int result = random.nextInt(MAX_NUMBER);
        while (result < MIN_NUMBER) {
            result = random.nextInt(MAX_NUMBER);
        }
        return result;
    }

    private String getCaptcha(int number) throws IOException {
        TextPaster paster = new SimpleTextPaster(4, 4, Color.white);
        BackgroundGenerator back = new GradientBackgroundGenerator(200, 100, Color.CYAN, Color.GRAY);
        FontGenerator font = new TwistedAndShearedRandomFontGenerator(30, null);
        WordGenerator words = new DummyWordGenerator(String.valueOf(number));
        WordToImage word2image = new ComposedWordToImage(font, back, paster);
        ImageCaptchaFactory factory = new GimpyFactory(words, word2image);
        ImageCaptcha pix = factory.getImageCaptcha();
        pix.getImageChallenge();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(pix.getImageChallenge(), "JPEG", stream);
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

    @Autowired
    private void startCleanTokensTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        new Timer().schedule(new CleanCaptchaTokensTimerTask(),
                calendar.getTime(), CLEAN_CAPTCHA_TOKENS_PERIOD);
    }

    private class CleanCaptchaTokensTimerTask extends TimerTask {

        @Override
        public void run() {
            Date minimalDate = getMinimalDate();
            captchaTokenRepository.cleanTokens(minimalDate);
        }

        private Date getMinimalDate() {
            Instant now = Instant.now();
            Instant before = now.minus(CAPTCHA_TOKEN_LIFETIME);
            return Date.from(before);
        }
    }
}
