package com.dataart.playme.configuration;

import com.dataart.playme.controller.binding.resolver.CurrentMusicianResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ArgumentsBindingConfiguration implements WebMvcConfigurer {

    private final CurrentMusicianResolver currentMusicianResolver;

    @Autowired
    public ArgumentsBindingConfiguration(CurrentMusicianResolver currentMusicianResolver) {
        this.currentMusicianResolver = currentMusicianResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMusicianResolver);
    }
}
