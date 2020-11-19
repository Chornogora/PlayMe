package com.dataart.playme.filter;

import com.dataart.playme.util.Constants;
import com.dataart.playme.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "SessionFilter")
public class SessionFilter implements Filter {

    private static final String LOGIN_PAGE_PATH = "/PlayMe/auth";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        if (isSessionActive(session)) {
            SessionUtil.refreshSession(session);
            chain.doFilter(request, response);
            return;
        }

        SessionUtil.invalidateSession(session, httpServletResponse);
        httpServletResponse.sendRedirect(LOGIN_PAGE_PATH);
    }

    private boolean isSessionActive(HttpSession session) {
        String attributeName = Constants.SESSION_LAST_ACTIVATION_TIME_ATTRIBUTE;
        Date lastActivatedDate = (Date) session.getAttribute(attributeName);
        if (lastActivatedDate == null) {
            return false;
        }
        long sessionLifetime = Long.parseLong(Constants.get(Constants.SESSION_LIFETIME));
        Date invalidationDate = new Date(lastActivatedDate.getTime() + sessionLifetime);
        return invalidationDate.compareTo(new Date(System.currentTimeMillis())) > 0;
    }
}
