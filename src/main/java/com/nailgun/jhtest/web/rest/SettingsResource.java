package com.nailgun.jhtest.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nailgun
 * @since 06.12.15
 */
@RestController
@RequestMapping("/api")
public class SettingsResource {

    @Value("${aws.s3.bucketName}")
    private String awsS3BucketName;

    /**
     * GET  /settings -> get application settings.
     */
    @RequestMapping(value = "/settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Map<String, Object> getSettings() {
        Map<String, Object> ret = new HashMap<>();
        ret.put("awsS3BucketName", awsS3BucketName);
        return ret;
    }

}
