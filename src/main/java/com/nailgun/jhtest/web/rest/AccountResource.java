package com.nailgun.jhtest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.nailgun.jhtest.domain.Authority;
import com.nailgun.jhtest.domain.User;
import com.nailgun.jhtest.repository.UserRepository;
import com.nailgun.jhtest.security.SecurityUtils;
import com.nailgun.jhtest.service.AuthenticationService;
import com.nailgun.jhtest.service.MailService;
import com.nailgun.jhtest.service.UserService;
import com.nailgun.jhtest.web.rest.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    @Inject
    Environment environment;

    @Inject
    private AuthenticationService authenticationService;

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        return userRepository.findOneByLogin(userDTO.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(userDTO.getEmail())
                .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
                    userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
                    userDTO.getLangKey());
                    String baseUrl = request.getScheme() + // "http"
                    "://" +                                // "://"
                    request.getServerName() +              // "myhost"
                    ":" +                                  // ":"
                    request.getServerPort();               // "80"

                    mailService.sendActivationEmail(user, baseUrl);
                    user.setActivated(true);//temporary activate user to get the access token
                    userRepository.save(user);
                    OAuth2AccessToken accessToken = authenticationService.getAccessToken(
                        userDTO.getLogin(), userDTO.getPassword());
                    user.setActivated(false);//deactivate again
                    user.setTokenData(authenticationService.createAccessTokenDbObject(accessToken));
                    userRepository.save(user);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
        );
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return Optional.ofNullable(userService.activateRegistration(key))
            .map(user -> user.map(u -> {
                    DBObject accessTokenData = u.getTokenData();
                    String accessTokenJson = JSON.serialize(accessTokenData);
                    u.setTokenData(null);
                    userRepository.save(u);
                    Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                    //HttpHeaders hh = new HttpHeaders();
                    //hh.add("token", accessTokenJson);
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(newAuth);
                    return new ResponseEntity<String>(HttpStatus.OK);
                }).orElse(new ResponseEntity<String>(HttpStatus.OK)))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> {
                return new ResponseEntity<>(
                    new UserDTO(
                        user.getLogin(),
                        null,
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getLangKey(),
                        user.getAuthorities().stream().map(Authority::getName)
                            .collect(Collectors.toList())),
                HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        return userRepository
            .findOneByLogin(userDTO.getLogin())
            .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentLogin()))
            .map(u -> {
                userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                    userDTO.getLangKey());
                return new ResponseEntity<String>(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {

        return userService.requestPasswordReset(mail)
            .map(user -> {
                String baseUrl = request.getScheme() +
                    "://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort();
            mailService.sendPasswordResetMail(user, baseUrl);
            return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/account/reset_password/finish",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestParam(value = "key") String key, @RequestParam(value = "newPassword") String newPassword) {
        if (!checkPasswordLength(newPassword)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(newPassword, key)
              .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
      return (!StringUtils.isEmpty(password) && password.length() >= UserDTO.PASSWORD_MIN_LENGTH && password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }

}
