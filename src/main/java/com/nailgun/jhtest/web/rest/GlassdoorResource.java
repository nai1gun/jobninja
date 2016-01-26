package com.nailgun.jhtest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nailgun.jhtest.service.GlassdoorService;
import com.nailgun.jhtest.web.rest.dto.GlassdoorEmployerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

/**
 *
 * REST controller for Glassdoor info
 *
 * @author nailgun
 * @since 25.01.16
 */
@Controller
@RequestMapping("/api")
public class GlassdoorResource {

    @Autowired
    private GlassdoorService glassdoorService;

    /**
     * GET  /glassdoor/:employerName -> get the "employerName" employer.
     */
    @RequestMapping(value = "/glassdoor/{employerName}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GlassdoorEmployerDTO> get(@PathVariable String employerName) {
        try {
            return Optional.ofNullable(glassdoorService.glassDoorUrl(employerName))
                .map(employer -> new ResponseEntity<>(
                    employer,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.OK));
        } catch (GlassdoorService.GlassdoorException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
