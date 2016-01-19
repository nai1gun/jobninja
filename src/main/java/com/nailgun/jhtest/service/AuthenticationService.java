package com.nailgun.jhtest.service;

import com.google.common.collect.Sets;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author nailgun
 * @since 14.01.16
 */
@Service
public class AuthenticationService {

    private static final String ENV_OAUTH = "authentication.oauth.";
    private static final String PROP_CLIENTID = "clientid";
    private static final String PROP_SECRET = "secret";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_CLIENT_ID = "client_id";
    private static final String PARAM_GRANT_TYPE = "grant_type"; ;
    private static final String PARAM_SCOPE = "scope";
    private static final String PARAM_CLIENT_SECRET = "client_secret";
    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final String PARAM_TOKEN_TYPE = "token_type";
    private static final String PARAM_REFRESH_TOKEN = "expires_in";
    private static final String PARAM_EXPIRES_IN = "expires_in";

    @Inject
    private TokenEndpoint tokenEndpoint;

    @Inject
    Environment environment;

    private RelaxedPropertyResolver propertyResolver;

    @PostConstruct
    private void initpropertyResolver() {
        propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
    }

    public DBObject createAccessTokenDbObject(OAuth2AccessToken accessToken) {
        BasicDBObject ret = new BasicDBObject();
        ret.append(PARAM_ACCESS_TOKEN, accessToken.getValue());
        ret.append(PARAM_TOKEN_TYPE, accessToken.getTokenType());
        ret.append(PARAM_REFRESH_TOKEN, accessToken.getRefreshToken().getValue());
        ret.append(PARAM_EXPIRES_IN, accessToken.getExpiresIn());
        ret.append(PARAM_SCOPE, StringUtils.join(accessToken.getScope(), ' '));
        return ret;
    }

    public String createAccessTokenJson(OAuth2AccessToken accessToken) {
        return JSON.serialize(createAccessTokenDbObject(accessToken));
    }

    public OAuth2AccessToken getAccessToken(String username, String password) {
        String clientId = propertyResolver.getProperty(PROP_CLIENTID);
        Map<String, String> authorizationParameters = new HashMap<>();
        authorizationParameters.put(PARAM_USERNAME, username);
        authorizationParameters.put(PARAM_PASSWORD, password);
        authorizationParameters.put(PARAM_CLIENT_ID, clientId);
        authorizationParameters.put(PARAM_GRANT_TYPE, "password");
        authorizationParameters.put(PARAM_SCOPE, "read write");
        authorizationParameters.put(PARAM_CLIENT_SECRET, propertyResolver.getProperty(PROP_SECRET));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Set<String> resourceIds = new HashSet<>();

        OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, clientId, authorities, true,
            Sets.newHashSet("read", "write"), resourceIds, "http://example.com", new HashSet<>(), null);

        // Create principal and auth token
        org.springframework.security.core.userdetails.User userPrincipal = new org.springframework.security.core.userdetails.User(
            clientId, "", true, true, true, true, authorities);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities) ;

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);
        ResponseEntity<OAuth2AccessToken> response;
        try {
            response = tokenEndpoint.postAccessToken(authenticationRequest, authorizationParameters);
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

}
