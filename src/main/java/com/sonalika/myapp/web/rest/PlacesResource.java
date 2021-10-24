package com.sonalika.myapp.web.rest;

import com.sonalika.myapp.domain.Places;
import com.sonalika.myapp.repository.PlacesRepository;
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
 * REST controller for managing {@link com.sonalika.myapp.domain.Places}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlacesResource {

    private final Logger log = LoggerFactory.getLogger(PlacesResource.class);

    private static final String ENTITY_NAME = "myAppPlaces";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlacesRepository placesRepository;

    public PlacesResource(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
    }

    /**
     * {@code POST  /places} : Create a new places.
     *
     * @param places the places to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new places, or with status {@code 400 (Bad Request)} if the places has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/places")
    public ResponseEntity<Places> createPlaces(@Valid @RequestBody Places places) throws URISyntaxException {
        log.debug("REST request to save Places : {}", places);
        if (places.getId() != null) {
            throw new BadRequestAlertException("A new places cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Places result = placesRepository.save(places);
        return ResponseEntity
            .created(new URI("/api/places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /places/:id} : Updates an existing places.
     *
     * @param id the id of the places to save.
     * @param places the places to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated places,
     * or with status {@code 400 (Bad Request)} if the places is not valid,
     * or with status {@code 500 (Internal Server Error)} if the places couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/places/{id}")
    public ResponseEntity<Places> updatePlaces(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Places places
    ) throws URISyntaxException {
        log.debug("REST request to update Places : {}, {}", id, places);
        if (places.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, places.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!placesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Places result = placesRepository.save(places);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, places.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /places/:id} : Partial updates given fields of an existing places, field will ignore if it is null
     *
     * @param id the id of the places to save.
     * @param places the places to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated places,
     * or with status {@code 400 (Bad Request)} if the places is not valid,
     * or with status {@code 404 (Not Found)} if the places is not found,
     * or with status {@code 500 (Internal Server Error)} if the places couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/places/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Places> partialUpdatePlaces(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Places places
    ) throws URISyntaxException {
        log.debug("REST request to partial update Places partially : {}, {}", id, places);
        if (places.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, places.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!placesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Places> result = placesRepository
            .findById(places.getId())
            .map(existingPlaces -> {
                if (places.getPlacesName() != null) {
                    existingPlaces.setPlacesName(places.getPlacesName());
                }
                if (places.getPlacesId() != null) {
                    existingPlaces.setPlacesId(places.getPlacesId());
                }
                if (places.getPlacesDesc() != null) {
                    existingPlaces.setPlacesDesc(places.getPlacesDesc());
                }
                if (places.getPlacesCurrentStatus() != null) {
                    existingPlaces.setPlacesCurrentStatus(places.getPlacesCurrentStatus());
                }
                if (places.getPlaceSpecialAttraction() != null) {
                    existingPlaces.setPlaceSpecialAttraction(places.getPlaceSpecialAttraction());
                }
                if (places.getPlaceVisitPrice() != null) {
                    existingPlaces.setPlaceVisitPrice(places.getPlaceVisitPrice());
                }

                return existingPlaces;
            })
            .map(placesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, places.getId().toString())
        );
    }

    /**
     * {@code GET  /places} : get all the places.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of places in body.
     */
    @GetMapping("/places")
    public ResponseEntity<List<Places>> getAllPlaces(Pageable pageable) {
        log.debug("REST request to get a page of Places");
        Page<Places> page = placesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /places/:id} : get the "id" places.
     *
     * @param id the id of the places to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the places, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/places/{id}")
    public ResponseEntity<Places> getPlaces(@PathVariable Long id) {
        log.debug("REST request to get Places : {}", id);
        Optional<Places> places = placesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(places);
    }

    /**
     * {@code DELETE  /places/:id} : delete the "id" places.
     *
     * @param id the id of the places to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/places/{id}")
    public ResponseEntity<Void> deletePlaces(@PathVariable Long id) {
        log.debug("REST request to delete Places : {}", id);
        placesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
