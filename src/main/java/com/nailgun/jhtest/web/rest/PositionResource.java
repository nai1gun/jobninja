package com.nailgun.jhtest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.nailgun.jhtest.domain.Position;
import com.nailgun.jhtest.domain.User;
import com.nailgun.jhtest.repository.PositionRepository;
import com.nailgun.jhtest.service.UserService;
import com.nailgun.jhtest.web.rest.util.HeaderUtil;
import com.nailgun.jhtest.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing Position.
 */
@RestController
@RequestMapping("/api")
public class PositionResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);

    @Inject
    private PositionRepository positionRepository;

    @Inject
    private UserService userService;

    @Inject
    private MessageSource messageSource;

    /**
     * POST  /positions -> Create a new position.
     */
    @RequestMapping(value = "/positions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Position> create(@RequestBody Position position) throws URISyntaxException {
        log.debug("REST request to save Position : {}", position);
        if (position.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new position cannot already have an ID").body(null);
        }
        User currentUser = userService.getUserWithAuthorities();
        position.setUser(currentUser);
        if (position.getState() == null) {
            Locale locale = Locale.forLanguageTag(currentUser.getLangKey());
            String createdState = messageSource.getMessage("position.state.created", null, locale);
            position.setState(createdState);
        }
        if (position.getCreated() == null) {
            position.setCreated(new DateTime());
        }
        if (position.getEdited() == null) {
            position.setEdited(new DateTime());
        }
        Position result = positionRepository.save(position);
        return ResponseEntity.created(new URI("/api/positions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("position", result.getId()))
                .body(result);
    }

    /**
     * PUT  /positions -> Updates an existing position.
     */
    @RequestMapping(value = "/positions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Position> update(@RequestBody Position position) throws URISyntaxException {
        log.debug("REST request to update Position : {}", position);
        if (position.getId() == null) {
            return create(position);
        }
        User currentUser = userService.getUserWithAuthorities();
        position.setUser(currentUser);
        Position result = positionRepository.save(position);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("position", position.getId()))
                .body(result);
    }

    /**
     * GET  /positions -> get all the positions.
     */
    @RequestMapping(value = "/positions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Position>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        User currentUser = userService.getUserWithAuthorities();
        Page<Position> page = positionRepository.findByUser(currentUser.getId(),
            PaginationUtil.generatePageRequest(offset, limit, Sort.Direction.DESC, "edited"));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/positions", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /positions/:id -> get the "id" position.
     */
    @RequestMapping(value = "/positions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Position> get(@PathVariable String id) {
        log.debug("REST request to get Position : {}", id);
        return Optional.ofNullable(positionRepository.findOne(id))
            .map(position -> new ResponseEntity<>(
                position,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /positions/find -> get all the positions.
     */
    @RequestMapping(value = "/positions/find",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Position>> findPositions(@RequestParam String link)
        throws URISyntaxException {
        User currentUser = userService.getUserWithAuthorities();
        List<Position> positions = positionRepository.findByLink(currentUser.getId(), link);
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

    /**
     * DELETE  /positions/:id -> delete the "id" position.
     */
    @RequestMapping(value = "/positions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("REST request to delete Position : {}", id);
        positionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("position", id)).build();
    }
}
