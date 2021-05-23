package com.dataart.playme.controller.binding.resolver;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.model.User;
import com.dataart.playme.service.MusicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentMusicianResolver implements HandlerMethodArgumentResolver {

    private final MusicianService musicianService;

    @Autowired
    public CurrentMusicianResolver(MusicianService musicianService) {
        this.musicianService = musicianService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentMusician.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return musicianService.findByUser(user);
    }
}
