package com.sonalika.myapp.web.rest;

import com.sonalika.myapp.domain.Facilities;
import com.sonalika.myapp.repository.FacilitiesRepository;
import com.sonalika.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonalika.myapp.domain.Facilities}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FacilitiesResource {

    private final Logger log = LoggerFactory.getLogger(FacilitiesResource.class);

    private static final String ENTITY_NAME = "myAppFacilities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacilitiesRepository facilitiesRepository;

    public FacilitiesResource(FacilitiesRepository facilitiesRepository) {
        this.facilitiesRepository = facilitiesRepository;
    }

    /**
     * {@code POST  /facilities} : Create a new facilities.
     *
     * @param facilities the facilities to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facilities, or with status {@code 400 (Bad Request)} if the facilities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facilities")
    public ResponseEntity<Facilities> createFacilities(@Valid @RequestBody Facilities facilities) throws URISyntaxException {
        log.debug("REST request to save Facilities : {}", facilities);
        if (facilities.getId() != null) {
            throw new BadRequestAlertException("A new facilities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Facilities result = facilitiesRepository.save(facilities);
        return ResponseEntity
            .created(new URI("/api/facilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /facilities/:id} : Updates an existing facilities.
     *
     * @param id the id of the facilities to save.
     * @param facilities the facilities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilities,
     * or with status {@code 400 (Bad Request)} if the facilities is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facilities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facilities/{id}")
    public ResponseEntity<Facilities> updateFacilities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Facilities facilities
    ) throws URISyntaxException {
        log.debug("REST request to update Facilities : {}, {}", id, facilities);
        if (facilities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Facilities result = facilitiesRepository.save(facilities);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facilities.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facilities/:id} : Partial updates given fields of an existing facilities, field will ignore if it is null
     *
     * @param id the id of the facilities to save.
     * @param facilities the facilities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facilities,
     * or with status {@code 400 (Bad Request)} if the facilities is not valid,
     * or with status {@code 404 (Not Found)} if the facilities is not found,
     * or with status {@code 500 (Internal Server Error)} if the facilities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Facilities> partialUpdateFacilities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Facilities facilities
    ) throws URISyntaxException {
        log.debug("REST request to partial update Facilities partially : {}, {}", id, facilities);
        if (facilities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facilities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facilitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Facilities> result = facilitiesRepository
            .findById(facilities.getId())
            .map(existingFacilities -> {
                if (facilities.getFacilitiesId() != null) {
                    existingFacilities.setFacilitiesId(facilities.getFacilitiesId());
                }
                if (facilities.getFacilitiesName() != null) {
                    existingFacilities.setFacilitiesName(facilities.getFacilitiesName());
                }
                if (facilities.getFacilitiesDesc() != null) {
                    existingFacilities.setFacilitiesDesc(facilities.getFacilitiesDesc());
                }
                if (facilities.getFacilitiesCharges() != null) {
                    existingFacilities.setFacilitiesCharges(facilities.getFacilitiesCharges());
                }

                return existingFacilities;
            })
            .map(facilitiesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, facilities.getId().toString())
        );
    }

    /**
     * {@code GET  /facilities} : get all the facilities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facilities in body.
     */
    @GetMapping("/facilities")
    public ResponseEntity<List<Facilities>> getAllFacilities(Pageable pageable) {
        log.debug("REST request to get a page of Facilities");
        Page<Facilities> page = facilitiesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /facilities/:id} : get the "id" facilities.
     *
     * @param id the id of the facilities to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facilities, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facilities/{id}")
    public ResponseEntity<Facilities> getFacilities(@PathVariable Long id) {
        log.debug("REST request to get Facilities : {}", id);
        Optional<Facilities> facilities = facilitiesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(facilities);
    }

    /**
     * {@code DELETE  /facilities/:id} : delete the "id" facilities.
     *
     * @param id the id of the facilities to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facilities/{id}")
    public ResponseEntity<Void> deleteFacilities(@PathVariable Long id) {
        log.debug("REST request to delete Facilities : {}", id);
        facilitiesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
