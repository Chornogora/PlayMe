package com.dataart.playme.configuration;

import com.dataart.playme.controller.binding.resolver.ActiveBandResolver;
import com.dataart.playme.controller.binding.resolver.CurrentMusicianResolver;
import com.dataart.playme.controller.binding.resolver.CurrentUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ArgumentsBindingConfiguration implements WebMvcConfigurer {

    private final CurrentMusicianResolver currentMusicianResolver;

    private final ActiveBandResolver activeBandResolver;

    private final CurrentUserResolver currentUserResolver;

    @Autowired
    public ArgumentsBindingConfiguration(CurrentMusicianResolver currentMusicianResolver,
                                         ActiveBandResolver activeBandResolver, CurrentUserResolver currentUserResolver) {
        this.currentMusicianResolver = currentMusicianResolver;
        this.activeBandResolver = activeBandResolver;
        this.currentUserResolver = currentUserResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(currentMusicianResolver, activeBandResolver, currentUserResolver));
    }
}
