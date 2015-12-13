package com.nailgun.jhtest.web.rest;

import com.nailgun.jhtest.Application;
import com.nailgun.jhtest.domain.CoverLetterTemplate;
import com.nailgun.jhtest.repository.CoverLetterTemplateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CoverLetterTemplateResource REST controller.
 *
 * @see CoverLetterTemplateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CoverLetterTemplateResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_TEXT = "UPDATED_TEXT";

    @Inject
    private CoverLetterTemplateRepository coverLetterTemplateRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restCoverLetterTemplateMockMvc;

    private CoverLetterTemplate coverLetterTemplate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverLetterTemplateResource coverLetterTemplateResource = new CoverLetterTemplateResource();
        ReflectionTestUtils.setField(coverLetterTemplateResource, "coverLetterTemplateRepository", coverLetterTemplateRepository);
        this.restCoverLetterTemplateMockMvc = MockMvcBuilders.standaloneSetup(coverLetterTemplateResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverLetterTemplateRepository.deleteAll();
        coverLetterTemplate = new CoverLetterTemplate();
        coverLetterTemplate.setName(DEFAULT_NAME);
        coverLetterTemplate.setText(DEFAULT_TEXT);
    }

    @Test
    public void createCoverLetterTemplate() throws Exception {
        int databaseSizeBeforeCreate = coverLetterTemplateRepository.findAll().size();

        // Create the CoverLetterTemplate

        restCoverLetterTemplateMockMvc.perform(post("/api/coverLetterTemplates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverLetterTemplate)))
                .andExpect(status().isCreated());

        // Validate the CoverLetterTemplate in the database
        List<CoverLetterTemplate> coverLetterTemplates = coverLetterTemplateRepository.findAll();
        assertThat(coverLetterTemplates).hasSize(databaseSizeBeforeCreate + 1);
        CoverLetterTemplate testCoverLetterTemplate = coverLetterTemplates.get(coverLetterTemplates.size() - 1);
        assertThat(testCoverLetterTemplate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCoverLetterTemplate.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    public void getAllCoverLetterTemplates() throws Exception {
        // Initialize the database
        coverLetterTemplateRepository.save(coverLetterTemplate);

        // Get all the coverLetterTemplates
        restCoverLetterTemplateMockMvc.perform(get("/api/coverLetterTemplates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverLetterTemplate.getId())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    public void getCoverLetterTemplate() throws Exception {
        // Initialize the database
        coverLetterTemplateRepository.save(coverLetterTemplate);

        // Get the coverLetterTemplate
        restCoverLetterTemplateMockMvc.perform(get("/api/coverLetterTemplates/{id}", coverLetterTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverLetterTemplate.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    public void getNonExistingCoverLetterTemplate() throws Exception {
        // Get the coverLetterTemplate
        restCoverLetterTemplateMockMvc.perform(get("/api/coverLetterTemplates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateCoverLetterTemplate() throws Exception {
        // Initialize the database
        coverLetterTemplateRepository.save(coverLetterTemplate);

		int databaseSizeBeforeUpdate = coverLetterTemplateRepository.findAll().size();

        // Update the coverLetterTemplate
        coverLetterTemplate.setName(UPDATED_NAME);
        coverLetterTemplate.setText(UPDATED_TEXT);


        restCoverLetterTemplateMockMvc.perform(put("/api/coverLetterTemplates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverLetterTemplate)))
                .andExpect(status().isOk());

        // Validate the CoverLetterTemplate in the database
        List<CoverLetterTemplate> coverLetterTemplates = coverLetterTemplateRepository.findAll();
        assertThat(coverLetterTemplates).hasSize(databaseSizeBeforeUpdate);
        CoverLetterTemplate testCoverLetterTemplate = coverLetterTemplates.get(coverLetterTemplates.size() - 1);
        assertThat(testCoverLetterTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCoverLetterTemplate.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    public void deleteCoverLetterTemplate() throws Exception {
        // Initialize the database
        coverLetterTemplateRepository.save(coverLetterTemplate);

		int databaseSizeBeforeDelete = coverLetterTemplateRepository.findAll().size();

        // Get the coverLetterTemplate
        restCoverLetterTemplateMockMvc.perform(delete("/api/coverLetterTemplates/{id}", coverLetterTemplate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CoverLetterTemplate> coverLetterTemplates = coverLetterTemplateRepository.findAll();
        assertThat(coverLetterTemplates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
