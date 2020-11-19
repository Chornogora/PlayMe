package com.dataart.playme.filter;

import com.dataart.playme.util.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HttpMethod;
import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter")
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        if (httpServletRequest.getMethod().equals(HttpMethod.GET)) {
            String role = (String) session.getAttribute(Constants.ROLE_ATTRIBUTE);
            if (isAdministrator(role)) {
                httpServletResponse.sendRedirect(Constants.ADMIN_MAIN_PAGE);
                return;
            } else if (isUser(role)) {
                httpServletResponse.sendRedirect(Constants.USER_MAIN_PAGE);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isAdministrator(String role) {
        String adminRole = Constants.get(Constants.ADMIN_ROLE_ID);
        return adminRole.equals(role);
    }

    private boolean isUser(String role) {
        String adminRole = Constants.get(Constants.USER_ROLE_ID);
        return adminRole.equals(role);
    }
}
