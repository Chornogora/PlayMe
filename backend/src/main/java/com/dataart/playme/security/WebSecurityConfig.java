package com.dataart.playme.security;

import com.dataart.playme.model.Role;
import com.dataart.playme.security.jwt.JwtSecurityConfigurer;
import com.dataart.playme.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/me").authenticated()
                .antMatchers(HttpMethod.GET, "/user", "/bands/**", "/posts/**", "/rehearsals",
                        "/rehearsals/**", "/tracks/**").hasRole(Role.RoleName.USER.getValue())
                .antMatchers(HttpMethod.GET, "/admin/**", "/bands/{\\d+}/_disable",
                        "/bands/{\\d+}/_enable").hasRole(Role.RoleName.ADMINISTRATOR.getValue())
                .and()
                .apply(new JwtSecurityConfigurer(jwtTokenProvider));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/image/**", "/webfonts/**")
                .and()
                .ignoring().antMatchers("/auth", "/signup/**", "/signup", "/signup/captcha", "/email/confirmation/**")
                .and()
                .ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
