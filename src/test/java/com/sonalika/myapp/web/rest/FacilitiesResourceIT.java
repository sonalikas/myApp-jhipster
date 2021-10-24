package com.sonalika.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sonalika.myapp.IntegrationTest;
import com.sonalika.myapp.domain.Facilities;
import com.sonalika.myapp.repository.FacilitiesRepository;
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
 * Integration tests for the {@link FacilitiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacilitiesResourceIT {

    private static final Long DEFAULT_FACILITIES_ID = 1L;
    private static final Long UPDATED_FACILITIES_ID = 2L;

    private static final String DEFAULT_FACILITIES_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FACILITIES_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FACILITIES_DESC = "AAAAAAAAAA";
    private static final String UPDATED_FACILITIES_DESC = "BBBBBBBBBB";

    private static final Float DEFAULT_FACILITIES_CHARGES = 1F;
    private static final Float UPDATED_FACILITIES_CHARGES = 2F;

    private static final String ENTITY_API_URL = "/api/facilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacilitiesRepository facilitiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacilitiesMockMvc;

    private Facilities facilities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facilities createEntity(EntityManager em) {
        Facilities facilities = new Facilities()
            .facilitiesId(DEFAULT_FACILITIES_ID)
            .facilitiesName(DEFAULT_FACILITIES_NAME)
            .facilitiesDesc(DEFAULT_FACILITIES_DESC)
            .facilitiesCharges(DEFAULT_FACILITIES_CHARGES);
        return facilities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Facilities createUpdatedEntity(EntityManager em) {
        Facilities facilities = new Facilities()
            .facilitiesId(UPDATED_FACILITIES_ID)
            .facilitiesName(UPDATED_FACILITIES_NAME)
            .facilitiesDesc(UPDATED_FACILITIES_DESC)
            .facilitiesCharges(UPDATED_FACILITIES_CHARGES);
        return facilities;
    }

    @BeforeEach
    public void initTest() {
        facilities = createEntity(em);
    }

    @Test
    @Transactional
    void createFacilities() throws Exception {
        int databaseSizeBeforeCreate = facilitiesRepository.findAll().size();
        // Create the Facilities
        restFacilitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facilities)))
            .andExpect(status().isCreated());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeCreate + 1);
        Facilities testFacilities = facilitiesList.get(facilitiesList.size() - 1);
        assertThat(testFacilities.getFacilitiesId()).isEqualTo(DEFAULT_FACILITIES_ID);
        assertThat(testFacilities.getFacilitiesName()).isEqualTo(DEFAULT_FACILITIES_NAME);
        assertThat(testFacilities.getFacilitiesDesc()).isEqualTo(DEFAULT_FACILITIES_DESC);
        assertThat(testFacilities.getFacilitiesCharges()).isEqualTo(DEFAULT_FACILITIES_CHARGES);
    }

    @Test
    @Transactional
    void createFacilitiesWithExistingId() throws Exception {
        // Create the Facilities with an existing ID
        facilities.setId(1L);

        int databaseSizeBeforeCreate = facilitiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacilitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facilities)))
            .andExpect(status().isBadRequest());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFacilitiesIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = facilitiesRepository.findAll().size();
        // set the field null
        facilities.setFacilitiesId(null);

        // Create the Facilities, which fails.

        restFacilitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facilities)))
            .andExpect(status().isBadRequest());

        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFacilitiesNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = facilitiesRepository.findAll().size();
        // set the field null
        facilities.setFacilitiesName(null);

        // Create the Facilities, which fails.

        restFacilitiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facilities)))
            .andExpect(status().isBadRequest());

        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFacilities() throws Exception {
        // Initialize the database
        facilitiesRepository.saveAndFlush(facilities);

        // Get all the facilitiesList
        restFacilitiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(facilities.getId().intValue())))
            .andExpect(jsonPath("$.[*].facilitiesId").value(hasItem(DEFAULT_FACILITIES_ID.intValue())))
            .andExpect(jsonPath("$.[*].facilitiesName").value(hasItem(DEFAULT_FACILITIES_NAME)))
            .andExpect(jsonPath("$.[*].facilitiesDesc").value(hasItem(DEFAULT_FACILITIES_DESC)))
            .andExpect(jsonPath("$.[*].facilitiesCharges").value(hasItem(DEFAULT_FACILITIES_CHARGES.doubleValue())));
    }

    @Test
    @Transactional
    void getFacilities() throws Exception {
        // Initialize the database
        facilitiesRepository.saveAndFlush(facilities);

        // Get the facilities
        restFacilitiesMockMvc
            .perform(get(ENTITY_API_URL_ID, facilities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(facilities.getId().intValue()))
            .andExpect(jsonPath("$.facilitiesId").value(DEFAULT_FACILITIES_ID.intValue()))
            .andExpect(jsonPath("$.facilitiesName").value(DEFAULT_FACILITIES_NAME))
            .andExpect(jsonPath("$.facilitiesDesc").value(DEFAULT_FACILITIES_DESC))
            .andExpect(jsonPath("$.facilitiesCharges").value(DEFAULT_FACILITIES_CHARGES.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFacilities() throws Exception {
        // Get the facilities
        restFacilitiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFacilities() throws Exception {
        // Initialize the database
        facilitiesRepository.saveAndFlush(facilities);

        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();

        // Update the facilities
        Facilities updatedFacilities = facilitiesRepository.findById(facilities.getId()).get();
        // Disconnect from session so that the updates on updatedFacilities are not directly saved in db
        em.detach(updatedFacilities);
        updatedFacilities
            .facilitiesId(UPDATED_FACILITIES_ID)
            .facilitiesName(UPDATED_FACILITIES_NAME)
            .facilitiesDesc(UPDATED_FACILITIES_DESC)
            .facilitiesCharges(UPDATED_FACILITIES_CHARGES);

        restFacilitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFacilities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFacilities))
            )
            .andExpect(status().isOk());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
        Facilities testFacilities = facilitiesList.get(facilitiesList.size() - 1);
        assertThat(testFacilities.getFacilitiesId()).isEqualTo(UPDATED_FACILITIES_ID);
        assertThat(testFacilities.getFacilitiesName()).isEqualTo(UPDATED_FACILITIES_NAME);
        assertThat(testFacilities.getFacilitiesDesc()).isEqualTo(UPDATED_FACILITIES_DESC);
        assertThat(testFacilities.getFacilitiesCharges()).isEqualTo(UPDATED_FACILITIES_CHARGES);
    }

    @Test
    @Transactional
    void putNonExistingFacilities() throws Exception {
        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();
        facilities.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facilities.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacilities() throws Exception {
        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();
        facilities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facilities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacilities() throws Exception {
        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();
        facilities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilitiesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facilities)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacilitiesWithPatch() throws Exception {
        // Initialize the database
        facilitiesRepository.saveAndFlush(facilities);

        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();

        // Update the facilities using partial update
        Facilities partialUpdatedFacilities = new Facilities();
        partialUpdatedFacilities.setId(facilities.getId());

        partialUpdatedFacilities
            .facilitiesName(UPDATED_FACILITIES_NAME)
            .facilitiesDesc(UPDATED_FACILITIES_DESC)
            .facilitiesCharges(UPDATED_FACILITIES_CHARGES);

        restFacilitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilities))
            )
            .andExpect(status().isOk());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
        Facilities testFacilities = facilitiesList.get(facilitiesList.size() - 1);
        assertThat(testFacilities.getFacilitiesId()).isEqualTo(DEFAULT_FACILITIES_ID);
        assertThat(testFacilities.getFacilitiesName()).isEqualTo(UPDATED_FACILITIES_NAME);
        assertThat(testFacilities.getFacilitiesDesc()).isEqualTo(UPDATED_FACILITIES_DESC);
        assertThat(testFacilities.getFacilitiesCharges()).isEqualTo(UPDATED_FACILITIES_CHARGES);
    }

    @Test
    @Transactional
    void fullUpdateFacilitiesWithPatch() throws Exception {
        // Initialize the database
        facilitiesRepository.saveAndFlush(facilities);

        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();

        // Update the facilities using partial update
        Facilities partialUpdatedFacilities = new Facilities();
        partialUpdatedFacilities.setId(facilities.getId());

        partialUpdatedFacilities
            .facilitiesId(UPDATED_FACILITIES_ID)
            .facilitiesName(UPDATED_FACILITIES_NAME)
            .facilitiesDesc(UPDATED_FACILITIES_DESC)
            .facilitiesCharges(UPDATED_FACILITIES_CHARGES);

        restFacilitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFacilities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFacilities))
            )
            .andExpect(status().isOk());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
        Facilities testFacilities = facilitiesList.get(facilitiesList.size() - 1);
        assertThat(testFacilities.getFacilitiesId()).isEqualTo(UPDATED_FACILITIES_ID);
        assertThat(testFacilities.getFacilitiesName()).isEqualTo(UPDATED_FACILITIES_NAME);
        assertThat(testFacilities.getFacilitiesDesc()).isEqualTo(UPDATED_FACILITIES_DESC);
        assertThat(testFacilities.getFacilitiesCharges()).isEqualTo(UPDATED_FACILITIES_CHARGES);
    }

    @Test
    @Transactional
    void patchNonExistingFacilities() throws Exception {
        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();
        facilities.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacilitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facilities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facilities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacilities() throws Exception {
        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();
        facilities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facilities))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacilities() throws Exception {
        int databaseSizeBeforeUpdate = facilitiesRepository.findAll().size();
        facilities.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacilitiesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facilities))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facilities in the database
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacilities() throws Exception {
        // Initialize the database
        facilitiesRepository.saveAndFlush(facilities);

        int databaseSizeBeforeDelete = facilitiesRepository.findAll().size();

        // Delete the facilities
        restFacilitiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, facilities.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Facilities> facilitiesList = facilitiesRepository.findAll();
        assertThat(facilitiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
