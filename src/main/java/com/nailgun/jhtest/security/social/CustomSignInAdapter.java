package com.nailgun.jhtest.security.social;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

public class CustomSignInAdapter implements SignInAdapter {

    @Inject
    private UserDetailsService userDetailsService;

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        UserDetails user = userDetailsService.loadUserByUsername(userId);
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        newAuth.setDetails(securityContext.getAuthentication().getDetails());
        securityContext.setAuthentication(newAuth);
        /*
        Hack with session to rewrite token in the future
         */
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        session.setAttribute("SECURITY_AUTHENTICATION", newAuth);
        return "/#/";// TODO: move to config
    }
}
