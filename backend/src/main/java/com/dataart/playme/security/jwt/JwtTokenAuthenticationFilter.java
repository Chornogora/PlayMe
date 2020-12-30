package com.dataart.playme.security.jwt;

import com.dataart.playme.util.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                refreshSession(auth, response);
            }
            filterChain.doFilter(req, res);
        } else {
            String authorizationPage = Constants.FRONTEND_AUTHORIZATION_PATH;
            ((HttpServletResponse) res).sendRedirect(authorizationPage);
        }
    }

    private void refreshSession(Authentication authentication, HttpServletResponse response) {
        String newToken = jwtTokenProvider.recreateToken(authentication);
        Cookie cookie = new Cookie(Constants.Security.JWT_TOKEN_COOKIE_NAME, newToken);
        cookie.setPath(Constants.APPLICATION_PATH);
        int expirationTimeSeconds = Integer.parseInt(
                Constants.get(Constants.Security.SESSION_LIFETIME)) / 1000;
        cookie.setMaxAge(expirationTimeSeconds);
        response.addCookie(cookie);
    }
}