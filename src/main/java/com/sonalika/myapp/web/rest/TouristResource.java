package com.sonalika.myapp.web.rest;

import com.sonalika.myapp.domain.Tourist;
import com.sonalika.myapp.repository.TouristRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sonalika.myapp.domain.Tourist}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TouristResource {

    private final Logger log = LoggerFactory.getLogger(TouristResource.class);

    private static final String ENTITY_NAME = "myAppTourist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TouristRepository touristRepository;

    public TouristResource(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    /**
     * {@code POST  /tourists} : Create a new tourist.
     *
     * @param tourist the tourist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tourist, or with status {@code 400 (Bad Request)} if the tourist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tourists")
    public ResponseEntity<Tourist> createTourist(@Valid @RequestBody Tourist tourist) throws URISyntaxException {
        log.debug("REST request to save Tourist : {}", tourist);
        if (tourist.getId() != null) {
            throw new BadRequestAlertException("A new tourist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tourist result = touristRepository.save(tourist);
        return ResponseEntity
            .created(new URI("/api/tourists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tourists/:id} : Updates an existing tourist.
     *
     * @param id the id of the tourist to save.
     * @param tourist the tourist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourist,
     * or with status {@code 400 (Bad Request)} if the tourist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tourist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tourists/{id}")
    public ResponseEntity<Tourist> updateTourist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Tourist tourist
    ) throws URISyntaxException {
        log.debug("REST request to update Tourist : {}, {}", id, tourist);
        if (tourist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tourist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!touristRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tourist result = touristRepository.save(tourist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tourist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tourists/:id} : Partial updates given fields of an existing tourist, field will ignore if it is null
     *
     * @param id the id of the tourist to save.
     * @param tourist the tourist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tourist,
     * or with status {@code 400 (Bad Request)} if the tourist is not valid,
     * or with status {@code 404 (Not Found)} if the tourist is not found,
     * or with status {@code 500 (Internal Server Error)} if the tourist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tourists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tourist> partialUpdateTourist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tourist tourist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tourist partially : {}, {}", id, tourist);
        if (tourist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tourist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!touristRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tourist> result = touristRepository
            .findById(tourist.getId())
            .map(existingTourist -> {
                if (tourist.getTouristName() != null) {
                    existingTourist.setTouristName(tourist.getTouristName());
                }
                if (tourist.getTouristId() != null) {
                    existingTourist.setTouristId(tourist.getTouristId());
                }
                if (tourist.getTouristFrom() != null) {
                    existingTourist.setTouristFrom(tourist.getTouristFrom());
                }
                if (tourist.getTouristImage() != null) {
                    existingTourist.setTouristImage(tourist.getTouristImage());
                }
                if (tourist.getTouristPan() != null) {
                    existingTourist.setTouristPan(tourist.getTouristPan());
                }
                if (tourist.getTouristAge() != null) {
                    existingTourist.setTouristAge(tourist.getTouristAge());
                }

                return existingTourist;
            })
            .map(touristRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tourist.getId().toString())
        );
    }

    /**
     * {@code GET  /tourists} : get all the tourists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tourists in body.
     */
    @GetMapping("/tourists")
    public List<Tourist> getAllTourists() {
        log.debug("REST request to get all Tourists");
        return touristRepository.findAll();
    }

    /**
     * {@code GET  /tourists/:id} : get the "id" tourist.
     *
     * @param id the id of the tourist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tourist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tourists/{id}")
    public ResponseEntity<Tourist> getTourist(@PathVariable Long id) {
        log.debug("REST request to get Tourist : {}", id);
        Optional<Tourist> tourist = touristRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tourist);
    }

    /**
     * {@code DELETE  /tourists/:id} : delete the "id" tourist.
     *
     * @param id the id of the tourist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tourists/{id}")
    public ResponseEntity<Void> deleteTourist(@PathVariable Long id) {
        log.debug("REST request to delete Tourist : {}", id);
        touristRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
