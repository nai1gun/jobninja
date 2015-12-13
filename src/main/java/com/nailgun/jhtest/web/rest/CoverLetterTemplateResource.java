package com.nailgun.jhtest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nailgun.jhtest.domain.CoverLetterTemplate;
import com.nailgun.jhtest.repository.CoverLetterTemplateRepository;
import com.nailgun.jhtest.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CoverLetterTemplate.
 */
@RestController
@RequestMapping("/api")
public class CoverLetterTemplateResource {

    private final Logger log = LoggerFactory.getLogger(CoverLetterTemplateResource.class);

    @Inject
    private CoverLetterTemplateRepository coverLetterTemplateRepository;

    /**
     * POST  /coverLetterTemplates -> Create a new coverLetterTemplate.
     */
    @RequestMapping(value = "/coverLetterTemplates",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverLetterTemplate> create(@RequestBody CoverLetterTemplate coverLetterTemplate) throws URISyntaxException {
        log.debug("REST request to save CoverLetterTemplate : {}", coverLetterTemplate);
        if (coverLetterTemplate.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new coverLetterTemplate cannot already have an ID").body(null);
        }
        CoverLetterTemplate result = coverLetterTemplateRepository.save(coverLetterTemplate);
        return ResponseEntity.created(new URI("/api/coverLetterTemplates/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("coverLetterTemplate", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /coverLetterTemplates -> Updates an existing coverLetterTemplate.
     */
    @RequestMapping(value = "/coverLetterTemplates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverLetterTemplate> update(@RequestBody CoverLetterTemplate coverLetterTemplate) throws URISyntaxException {
        log.debug("REST request to update CoverLetterTemplate : {}", coverLetterTemplate);
        if (coverLetterTemplate.getId() == null) {
            return create(coverLetterTemplate);
        }
        CoverLetterTemplate result = coverLetterTemplateRepository.save(coverLetterTemplate);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("coverLetterTemplate", coverLetterTemplate.getId().toString()))
                .body(result);
    }

    /**
     * GET  /coverLetterTemplates -> get all the coverLetterTemplates.
     */
    @RequestMapping(value = "/coverLetterTemplates",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverLetterTemplate> getAll() {
        log.debug("REST request to get all CoverLetterTemplates");
        return coverLetterTemplateRepository.findAll();
    }

    /**
     * GET  /coverLetterTemplates/:id -> get the "id" coverLetterTemplate.
     */
    @RequestMapping(value = "/coverLetterTemplates/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverLetterTemplate> get(@PathVariable String id) {
        log.debug("REST request to get CoverLetterTemplate : {}", id);
        return Optional.ofNullable(coverLetterTemplateRepository.findOne(id))
            .map(coverLetterTemplate -> new ResponseEntity<>(
                coverLetterTemplate,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverLetterTemplates/:id -> delete the "id" coverLetterTemplate.
     */
    @RequestMapping(value = "/coverLetterTemplates/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("REST request to delete CoverLetterTemplate : {}", id);
        coverLetterTemplateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverLetterTemplate", id.toString())).build();
    }
}
