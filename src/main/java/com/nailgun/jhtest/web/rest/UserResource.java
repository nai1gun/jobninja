package com.nailgun.jhtest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nailgun.jhtest.domain.Cv;
import com.nailgun.jhtest.repository.UserRepository;
import com.nailgun.jhtest.security.SecurityUtils;
import com.nailgun.jhtest.service.UserService;
import org.jets3t.service.S3Service;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private static final String AWS_BUCKET_CV_DIR = "cv";

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${aws.accessKeyId:#{null}}")
    private String awsAccessKey;

    @Value("${aws.secretKey:#{null}}")
    private String awsSecretKey;

    @Value("${aws.s3.bucketName}")
    private String awsS3BucketName;

    private AWSCredentials awsCredentials;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @PostConstruct
    private void initAwsCredentials() {
        awsCredentials = new AWSCredentials(awsAccessKey, awsSecretKey);
    }

    /**
     * GET  /users -> get all users.
     */
    /*@RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll() {
        log.debug("REST request to get all Users");
        return userRepository.findAll();
    }*/

    /**
     * GET  /users/:login -> get the "login" user.
     */
    /*@RequestMapping(value = "/users/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findOneByLogin(login)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/

    /**
     * POST  /users/current/cv -> Upload cv.
     */
    @RequestMapping(value = "/users/current/cv",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cv> createCv(@RequestParam("file") MultipartFile file,
                                       @RequestHeader @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime created) {
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String dir = AWS_BUCKET_CV_DIR + "/" + UUID.randomUUID().toString() + "/";
                String filePath = dir + name;
                S3Service s3Service = new RestS3Service(awsCredentials);
                S3Object s3Object = new S3Object(filePath);
                s3Object.addMetadata("Cache-Control", "max-age=1314000");
                s3Object.setContentType(file.getContentType());
                s3Object.setContentLength(bytes.length);
                s3Object.setDataInputStream(new ByteArrayInputStream(bytes));
                s3Object.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
                S3Bucket dataBucket = s3Service.getBucket(awsS3BucketName);
                s3Service.putObject(dataBucket, s3Object);

                Cv cv = new Cv();
                cv.setFilePath(filePath);
                cv.setCreated(created);
                userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(user -> {
                    if (user.getCvs() == null) {
                        user.setCvs(new ArrayList<>());
                    }
                    List<Cv> cvs = user.getCvs();
                    cvs.add(cv);
                    userRepository.save(user);
                });

                return ResponseEntity.ok(cv);
            } catch (Exception e) {
                log.error("Exception while upload", e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET  /users/current/cv -> Upload cv.
     */
    @RequestMapping(value = "/users/current/cv",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cv> getCvs() {
        return userService.getUserWithAuthorities().getCvs();
    }

    /**
     * DELETE  /users/current/cv -> delete the Cv from current user's collection.
     */
    @RequestMapping(value = "/users/current/cv",
        method = RequestMethod.PUT, //DELETE method doesn't allow request body in Angular
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@RequestBody Cv cv) {
        if (cv == null) {
            return ResponseEntity.badRequest().build();
        }
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(user -> {
            List<Cv> cvs = user.getCvs();
            if (cvs != null) {
                cvs.removeIf((Cv currentCv) -> currentCv.equals(cv));
                userRepository.save(user);
            }
        });
        return ResponseEntity.ok().build();
    }
}
