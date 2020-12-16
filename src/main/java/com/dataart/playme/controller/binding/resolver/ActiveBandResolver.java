package com.dataart.playme.controller.binding.resolver;

import com.dataart.playme.controller.binding.annotation.ActiveBand;
import com.dataart.playme.exception.ConflictException;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Band;
import com.dataart.playme.repository.BandRepository;
import com.dataart.playme.service.BandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.ws.rs.BadRequestException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActiveBandResolver implements HandlerMethodArgumentResolver {

    public static final String BAND_ID = "bandId";

    private final BandService bandService;

    private final BandRepository bandRepository;

    @Autowired
    public ActiveBandResolver(BandService bandService, BandRepository bandRepository) {
        this.bandService = bandService;
        this.bandRepository = bandRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ActiveBand.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String path = ((ServletWebRequest) webRequest).getRequest().getServletPath();
        Pattern pattern = Pattern.compile("/bands/(?<bandId>[^/]*)/?");
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            String bandId = matcher.group(BAND_ID);
            Band band = bandRepository.findById(bandId)
                    .orElseThrow(() -> new NoSuchRecordException("Band was not found"));
            if (bandService.isActiveBand(band)) {
                return band;
            }
            throw new ConflictException("Band is disabled");
        }
        throw new BadRequestException("No bandId provided");
    }
}