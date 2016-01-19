package com.nailgun.jhtest.security.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * Rewrites the auth token with the one got from the session
 * (see com.nailgun.jhtest.security.social.CustomSignInAdapter)
 * to fix the mysterious behaviour of Spring Security
 *
 * @author nailgun
 * @since 16.01.16
 */
@Component
public class SocialAuthFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(SocialAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object authObj = session.getAttribute("SECURITY_AUTHENTICATION");
        if (authObj != null && authObj instanceof Authentication) {
            Authentication auth = (Authentication) authObj;
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Giving access to social user");
        }
        chain.doFilter(request, response);
    }

}
