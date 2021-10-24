package com.sonalika.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonalika.myapp.IntegrationTest;
import com.sonalika.myapp.domain.Tourist;
import com.sonalika.myapp.repository.TouristRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TouristResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TouristResourceIT {

    private static final String DEFAULT_TOURIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TOURIST_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TOURIST_ID = 1L;
    private static final Long UPDATED_TOURIST_ID = 2L;

    private static final String DEFAULT_TOURIST_FROM = "AAAAAAAAAA";
    private static final String UPDATED_TOURIST_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_TOURIST_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_TOURIST_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TOURIST_PAN = "AAAAAAAAAA";
    private static final String UPDATED_TOURIST_PAN = "BBBBBBBBBB";

    private static final String DEFAULT_TOURIST_AGE = "AAAAAAAAAA";
    private static final String UPDATED_TOURIST_AGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tourists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTouristMockMvc;

    private Tourist tourist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tourist createEntity(EntityManager em) {
        Tourist tourist = new Tourist()
            .touristName(DEFAULT_TOURIST_NAME)
            .touristId(DEFAULT_TOURIST_ID)
            .touristFrom(DEFAULT_TOURIST_FROM)
            .touristImage(DEFAULT_TOURIST_IMAGE)
            .touristPan(DEFAULT_TOURIST_PAN)
            .touristAge(DEFAULT_TOURIST_AGE);
        return tourist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tourist createUpdatedEntity(EntityManager em) {
        Tourist tourist = new Tourist()
            .touristName(UPDATED_TOURIST_NAME)
            .touristId(UPDATED_TOURIST_ID)
            .touristFrom(UPDATED_TOURIST_FROM)
            .touristImage(UPDATED_TOURIST_IMAGE)
            .touristPan(UPDATED_TOURIST_PAN)
            .touristAge(UPDATED_TOURIST_AGE);
        return tourist;
    }

    @BeforeEach
    public void initTest() {
        tourist = createEntity(em);
    }

    @Test
    @Transactional
    void createTourist() throws Exception {
        int databaseSizeBeforeCreate = touristRepository.findAll().size();
        // Create the Tourist
        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isCreated());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeCreate + 1);
        Tourist testTourist = touristList.get(touristList.size() - 1);
        assertThat(testTourist.getTouristName()).isEqualTo(DEFAULT_TOURIST_NAME);
        assertThat(testTourist.getTouristId()).isEqualTo(DEFAULT_TOURIST_ID);
        assertThat(testTourist.getTouristFrom()).isEqualTo(DEFAULT_TOURIST_FROM);
        assertThat(testTourist.getTouristImage()).isEqualTo(DEFAULT_TOURIST_IMAGE);
        assertThat(testTourist.getTouristPan()).isEqualTo(DEFAULT_TOURIST_PAN);
        assertThat(testTourist.getTouristAge()).isEqualTo(DEFAULT_TOURIST_AGE);
    }

    @Test
    @Transactional
    void createTouristWithExistingId() throws Exception {
        // Create the Tourist with an existing ID
        tourist.setId(1L);

        int databaseSizeBeforeCreate = touristRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isBadRequest());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTouristNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setTouristName(null);

        // Create the Tourist, which fails.

        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isBadRequest());

        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTouristIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setTouristId(null);

        // Create the Tourist, which fails.

        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isBadRequest());

        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTouristImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setTouristImage(null);

        // Create the Tourist, which fails.

        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isBadRequest());

        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTouristPanIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setTouristPan(null);

        // Create the Tourist, which fails.

        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isBadRequest());

        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTouristAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = touristRepository.findAll().size();
        // set the field null
        tourist.setTouristAge(null);

        // Create the Tourist, which fails.

        restTouristMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isBadRequest());

        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTourists() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        // Get all the touristList
        restTouristMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tourist.getId().intValue())))
            .andExpect(jsonPath("$.[*].touristName").value(hasItem(DEFAULT_TOURIST_NAME)))
            .andExpect(jsonPath("$.[*].touristId").value(hasItem(DEFAULT_TOURIST_ID.intValue())))
            .andExpect(jsonPath("$.[*].touristFrom").value(hasItem(DEFAULT_TOURIST_FROM)))
            .andExpect(jsonPath("$.[*].touristImage").value(hasItem(DEFAULT_TOURIST_IMAGE)))
            .andExpect(jsonPath("$.[*].touristPan").value(hasItem(DEFAULT_TOURIST_PAN)))
            .andExpect(jsonPath("$.[*].touristAge").value(hasItem(DEFAULT_TOURIST_AGE)));
    }

    @Test
    @Transactional
    void getTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        // Get the tourist
        restTouristMockMvc
            .perform(get(ENTITY_API_URL_ID, tourist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tourist.getId().intValue()))
            .andExpect(jsonPath("$.touristName").value(DEFAULT_TOURIST_NAME))
            .andExpect(jsonPath("$.touristId").value(DEFAULT_TOURIST_ID.intValue()))
            .andExpect(jsonPath("$.touristFrom").value(DEFAULT_TOURIST_FROM))
            .andExpect(jsonPath("$.touristImage").value(DEFAULT_TOURIST_IMAGE))
            .andExpect(jsonPath("$.touristPan").value(DEFAULT_TOURIST_PAN))
            .andExpect(jsonPath("$.touristAge").value(DEFAULT_TOURIST_AGE));
    }

    @Test
    @Transactional
    void getNonExistingTourist() throws Exception {
        // Get the tourist
        restTouristMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        int databaseSizeBeforeUpdate = touristRepository.findAll().size();

        // Update the tourist
        Tourist updatedTourist = touristRepository.findById(tourist.getId()).get();
        // Disconnect from session so that the updates on updatedTourist are not directly saved in db
        em.detach(updatedTourist);
        updatedTourist
            .touristName(UPDATED_TOURIST_NAME)
            .touristId(UPDATED_TOURIST_ID)
            .touristFrom(UPDATED_TOURIST_FROM)
            .touristImage(UPDATED_TOURIST_IMAGE)
            .touristPan(UPDATED_TOURIST_PAN)
            .touristAge(UPDATED_TOURIST_AGE);

        restTouristMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTourist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTourist))
            )
            .andExpect(status().isOk());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
        Tourist testTourist = touristList.get(touristList.size() - 1);
        assertThat(testTourist.getTouristName()).isEqualTo(UPDATED_TOURIST_NAME);
        assertThat(testTourist.getTouristId()).isEqualTo(UPDATED_TOURIST_ID);
        assertThat(testTourist.getTouristFrom()).isEqualTo(UPDATED_TOURIST_FROM);
        assertThat(testTourist.getTouristImage()).isEqualTo(UPDATED_TOURIST_IMAGE);
        assertThat(testTourist.getTouristPan()).isEqualTo(UPDATED_TOURIST_PAN);
        assertThat(testTourist.getTouristAge()).isEqualTo(UPDATED_TOURIST_AGE);
    }

    @Test
    @Transactional
    void putNonExistingTourist() throws Exception {
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();
        tourist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTouristMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tourist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tourist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTourist() throws Exception {
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();
        tourist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTouristMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tourist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTourist() throws Exception {
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();
        tourist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTouristMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTouristWithPatch() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        int databaseSizeBeforeUpdate = touristRepository.findAll().size();

        // Update the tourist using partial update
        Tourist partialUpdatedTourist = new Tourist();
        partialUpdatedTourist.setId(tourist.getId());

        partialUpdatedTourist
            .touristName(UPDATED_TOURIST_NAME)
            .touristFrom(UPDATED_TOURIST_FROM)
            .touristImage(UPDATED_TOURIST_IMAGE)
            .touristPan(UPDATED_TOURIST_PAN)
            .touristAge(UPDATED_TOURIST_AGE);

        restTouristMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTourist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTourist))
            )
            .andExpect(status().isOk());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
        Tourist testTourist = touristList.get(touristList.size() - 1);
        assertThat(testTourist.getTouristName()).isEqualTo(UPDATED_TOURIST_NAME);
        assertThat(testTourist.getTouristId()).isEqualTo(DEFAULT_TOURIST_ID);
        assertThat(testTourist.getTouristFrom()).isEqualTo(UPDATED_TOURIST_FROM);
        assertThat(testTourist.getTouristImage()).isEqualTo(UPDATED_TOURIST_IMAGE);
        assertThat(testTourist.getTouristPan()).isEqualTo(UPDATED_TOURIST_PAN);
        assertThat(testTourist.getTouristAge()).isEqualTo(UPDATED_TOURIST_AGE);
    }

    @Test
    @Transactional
    void fullUpdateTouristWithPatch() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        int databaseSizeBeforeUpdate = touristRepository.findAll().size();

        // Update the tourist using partial update
        Tourist partialUpdatedTourist = new Tourist();
        partialUpdatedTourist.setId(tourist.getId());

        partialUpdatedTourist
            .touristName(UPDATED_TOURIST_NAME)
            .touristId(UPDATED_TOURIST_ID)
            .touristFrom(UPDATED_TOURIST_FROM)
            .touristImage(UPDATED_TOURIST_IMAGE)
            .touristPan(UPDATED_TOURIST_PAN)
            .touristAge(UPDATED_TOURIST_AGE);

        restTouristMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTourist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTourist))
            )
            .andExpect(status().isOk());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
        Tourist testTourist = touristList.get(touristList.size() - 1);
        assertThat(testTourist.getTouristName()).isEqualTo(UPDATED_TOURIST_NAME);
        assertThat(testTourist.getTouristId()).isEqualTo(UPDATED_TOURIST_ID);
        assertThat(testTourist.getTouristFrom()).isEqualTo(UPDATED_TOURIST_FROM);
        assertThat(testTourist.getTouristImage()).isEqualTo(UPDATED_TOURIST_IMAGE);
        assertThat(testTourist.getTouristPan()).isEqualTo(UPDATED_TOURIST_PAN);
        assertThat(testTourist.getTouristAge()).isEqualTo(UPDATED_TOURIST_AGE);
    }

    @Test
    @Transactional
    void patchNonExistingTourist() throws Exception {
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();
        tourist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTouristMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tourist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tourist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTourist() throws Exception {
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();
        tourist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTouristMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tourist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTourist() throws Exception {
        int databaseSizeBeforeUpdate = touristRepository.findAll().size();
        tourist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTouristMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tourist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tourist in the database
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTourist() throws Exception {
        // Initialize the database
        touristRepository.saveAndFlush(tourist);

        int databaseSizeBeforeDelete = touristRepository.findAll().size();

        // Delete the tourist
        restTouristMockMvc
            .perform(delete(ENTITY_API_URL_ID, tourist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tourist> touristList = touristRepository.findAll();
        assertThat(touristList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
