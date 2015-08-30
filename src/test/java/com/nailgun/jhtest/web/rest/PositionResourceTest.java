package com.nailgun.jhtest.web.rest;

import com.nailgun.jhtest.Application;
import com.nailgun.jhtest.domain.Position;
import com.nailgun.jhtest.repository.PositionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PositionResource REST controller.
 *
 * @see PositionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PositionResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LINK = "SAMPLE_TEXT";
    private static final String UPDATED_LINK = "UPDATED_TEXT";
    private static final String DEFAULT_STATE = "SAMPLE_TEXT";
    private static final String UPDATED_STATE = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_STR = dateTimeFormatter.print(DEFAULT_CREATED);

    private static final DateTime DEFAULT_EDITED = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_EDITED = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_EDITED_STR = dateTimeFormatter.print(DEFAULT_EDITED);
    private static final String DEFAULT_NOTES = "SAMPLE_TEXT";
    private static final String UPDATED_NOTES = "UPDATED_TEXT";

    @Inject
    private PositionRepository positionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restPositionMockMvc;

    private Position position;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PositionResource positionResource = new PositionResource();
        ReflectionTestUtils.setField(positionResource, "positionRepository", positionRepository);
        this.restPositionMockMvc = MockMvcBuilders.standaloneSetup(positionResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        positionRepository.deleteAll();
        position = new Position();
        position.setName(DEFAULT_NAME);
        position.setLink(DEFAULT_LINK);
        position.setState(DEFAULT_STATE);
        position.setCreated(DEFAULT_CREATED);
        position.setEdited(DEFAULT_EDITED);
        position.setNotes(DEFAULT_NOTES);
    }

    @Test
    public void createPosition() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();

        // Create the Position

        restPositionMockMvc.perform(post("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeCreate + 1);
        Position testPosition = positions.get(positions.size() - 1);
        assertThat(testPosition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPosition.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testPosition.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPosition.getCreated().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED);
        assertThat(testPosition.getEdited().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_EDITED);
        assertThat(testPosition.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    public void getAllPositions() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get all the positions
        restPositionMockMvc.perform(get("/api/positions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(position.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED_STR)))
                .andExpect(jsonPath("$.[*].edited").value(hasItem(DEFAULT_EDITED_STR)))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    public void getPosition() throws Exception {
        // Initialize the database
        positionRepository.save(position);

        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", position.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(position.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED_STR))
            .andExpect(jsonPath("$.edited").value(DEFAULT_EDITED_STR))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    public void getNonExistingPosition() throws Exception {
        // Get the position
        restPositionMockMvc.perform(get("/api/positions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePosition() throws Exception {
        // Initialize the database
        positionRepository.save(position);

		int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Update the position
        position.setName(UPDATED_NAME);
        position.setLink(UPDATED_LINK);
        position.setState(UPDATED_STATE);
        position.setCreated(UPDATED_CREATED);
        position.setEdited(UPDATED_EDITED);
        position.setNotes(UPDATED_NOTES);
        

        restPositionMockMvc.perform(put("/api/positions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(position)))
                .andExpect(status().isOk());

        // Validate the Position in the database
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeUpdate);
        Position testPosition = positions.get(positions.size() - 1);
        assertThat(testPosition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPosition.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testPosition.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPosition.getCreated().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED);
        assertThat(testPosition.getEdited().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_EDITED);
        assertThat(testPosition.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    public void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.save(position);

		int databaseSizeBeforeDelete = positionRepository.findAll().size();

        // Get the position
        restPositionMockMvc.perform(delete("/api/positions/{id}", position.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Position> positions = positionRepository.findAll();
        assertThat(positions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
