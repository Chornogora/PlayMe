package com.dataart.playme.security.jwt;

import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class JwtTokenProvider {

    private static final String AUTHORIZATION_REQUEST_HEADER = "Authorization";

    private static final String BEARER_MARK = "Bearer ";

    private static final String JWT_TOKEN_COOKIE_NAME = "token";

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    private String secretKey;

    @Autowired
    public JwtTokenProvider(JwtProperties jwtProperties, @Qualifier("userSecurityService") UserDetailsService userDetailsService) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMs());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String login = getLogin(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(login);
        return new UsernamePasswordAuthenticationToken(userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
    }

    public String getLogin(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION_REQUEST_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_MARK)) {
            return bearerToken.substring(BEARER_MARK.length());
        }
        return getTokenFromCookie(req);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

    private String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            return Stream.of(cookies)
                    .filter(cookie -> cookie.getName().equals(JWT_TOKEN_COOKIE_NAME))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}