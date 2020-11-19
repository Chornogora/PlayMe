package com.dataart.playme.filter;

import com.dataart.playme.util.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import java.io.IOException;

@WebFilter(filterName = "UserFilter")
public class UserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();

        String role = (String) session.getAttribute(Constants.ROLE_ATTRIBUTE);
        String userRole = Constants.get(Constants.USER_ROLE_ID);

        if (userRole.equals(role)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.sendError(Response.Status.FORBIDDEN.getStatusCode());
    }
}
