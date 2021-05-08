package br.com.qrdapio.web.rest;

import br.com.qrdapio.domain.Restaurante;
import br.com.qrdapio.repository.RestauranteRepository;
import br.com.qrdapio.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link br.com.qrdapio.domain.Restaurante}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RestauranteResource {

    private final Logger log = LoggerFactory.getLogger(RestauranteResource.class);

    private static final String ENTITY_NAME = "restaurante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestauranteRepository restauranteRepository;

    public RestauranteResource(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    /**
     * {@code POST  /restaurantes} : Create a new restaurante.
     *
     * @param restaurante the restaurante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurante, or with status {@code 400 (Bad Request)} if the restaurante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurantes")
    public ResponseEntity<Restaurante> createRestaurante(@Valid @RequestBody Restaurante restaurante) throws URISyntaxException {
        log.debug("REST request to save Restaurante : {}", restaurante);
        if (restaurante.getId() != null) {
            throw new BadRequestAlertException("A new restaurante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Restaurante result = restauranteRepository.save(restaurante);
        return ResponseEntity
            .created(new URI("/api/restaurantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurantes/:id} : Updates an existing restaurante.
     *
     * @param id the id of the restaurante to save.
     * @param restaurante the restaurante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurante,
     * or with status {@code 400 (Bad Request)} if the restaurante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurantes/{id}")
    public ResponseEntity<Restaurante> updateRestaurante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Restaurante restaurante
    ) throws URISyntaxException {
        log.debug("REST request to update Restaurante : {}, {}", id, restaurante);
        if (restaurante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restauranteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Restaurante result = restauranteRepository.save(restaurante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurantes/:id} : Partial updates given fields of an existing restaurante, field will ignore if it is null
     *
     * @param id the id of the restaurante to save.
     * @param restaurante the restaurante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurante,
     * or with status {@code 400 (Bad Request)} if the restaurante is not valid,
     * or with status {@code 404 (Not Found)} if the restaurante is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurantes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Restaurante> partialUpdateRestaurante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Restaurante restaurante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaurante partially : {}, {}", id, restaurante);
        if (restaurante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restauranteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Restaurante> result = restauranteRepository
            .findById(restaurante.getId())
            .map(
                existingRestaurante -> {
                    if (restaurante.getNome() != null) {
                        existingRestaurante.setNome(restaurante.getNome());
                    }

                    return existingRestaurante;
                }
            )
            .map(restauranteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurante.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurantes} : get all the restaurantes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantes in body.
     */
    @GetMapping("/restaurantes")
    public List<Restaurante> getAllRestaurantes() {
        log.debug("REST request to get all Restaurantes");
        return restauranteRepository.findAll();
    }

    /**
     * {@code GET  /restaurantes/:id} : get the "id" restaurante.
     *
     * @param id the id of the restaurante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurantes/{id}")
    public ResponseEntity<Restaurante> getRestaurante(@PathVariable Long id) {
        log.debug("REST request to get Restaurante : {}", id);
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(restaurante);
    }

    /**
     * {@code DELETE  /restaurantes/:id} : delete the "id" restaurante.
     *
     * @param id the id of the restaurante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurantes/{id}")
    public ResponseEntity<Void> deleteRestaurante(@PathVariable Long id) {
        log.debug("REST request to delete Restaurante : {}", id);
        restauranteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
