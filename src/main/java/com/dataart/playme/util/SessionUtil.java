package com.dataart.playme.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

public final class SessionUtil {

    private static final String SESSION_ID_COOKIE_NAME = "JSESSIONID";

    public static void refreshSession(HttpSession session) {
        Date now = new Date(System.currentTimeMillis());
        long sessionLifetime = Long.parseLong(Constants.get(Constants.SESSION_LIFETIME));
        Date invalidationDate = new Date(now.getTime() + sessionLifetime);

        String attributeName = Constants.SESSION_LAST_ACTIVATION_TIME_ATTRIBUTE;
        session.setAttribute(attributeName, invalidationDate);
    }

    public static void invalidateSession(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = new Cookie(SESSION_ID_COOKIE_NAME, StringUtils.EMPTY);
        cookie.setMaxAge(0);
        cookie.setPath(Constants.APPLICATION_PATH);
        response.addCookie(cookie);
    }
}
