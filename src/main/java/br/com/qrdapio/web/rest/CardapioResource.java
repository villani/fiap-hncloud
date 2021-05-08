package br.com.qrdapio.web.rest;

import br.com.qrdapio.domain.Cardapio;
import br.com.qrdapio.repository.CardapioRepository;
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
 * REST controller for managing {@link br.com.qrdapio.domain.Cardapio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CardapioResource {

    private final Logger log = LoggerFactory.getLogger(CardapioResource.class);

    private static final String ENTITY_NAME = "cardapio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardapioRepository cardapioRepository;

    public CardapioResource(CardapioRepository cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    /**
     * {@code POST  /cardapios} : Create a new cardapio.
     *
     * @param cardapio the cardapio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardapio, or with status {@code 400 (Bad Request)} if the cardapio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cardapios")
    public ResponseEntity<Cardapio> createCardapio(@Valid @RequestBody Cardapio cardapio) throws URISyntaxException {
        log.debug("REST request to save Cardapio : {}", cardapio);
        if (cardapio.getId() != null) {
            throw new BadRequestAlertException("A new cardapio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cardapio result = cardapioRepository.save(cardapio);
        return ResponseEntity
            .created(new URI("/api/cardapios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cardapios/:id} : Updates an existing cardapio.
     *
     * @param id the id of the cardapio to save.
     * @param cardapio the cardapio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardapio,
     * or with status {@code 400 (Bad Request)} if the cardapio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardapio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cardapios/{id}")
    public ResponseEntity<Cardapio> updateCardapio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cardapio cardapio
    ) throws URISyntaxException {
        log.debug("REST request to update Cardapio : {}, {}", id, cardapio);
        if (cardapio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardapio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardapioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cardapio result = cardapioRepository.save(cardapio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardapio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cardapios/:id} : Partial updates given fields of an existing cardapio, field will ignore if it is null
     *
     * @param id the id of the cardapio to save.
     * @param cardapio the cardapio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardapio,
     * or with status {@code 400 (Bad Request)} if the cardapio is not valid,
     * or with status {@code 404 (Not Found)} if the cardapio is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardapio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cardapios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Cardapio> partialUpdateCardapio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cardapio cardapio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cardapio partially : {}, {}", id, cardapio);
        if (cardapio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardapio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardapioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cardapio> result = cardapioRepository
            .findById(cardapio.getId())
            .map(
                existingCardapio -> {
                    if (cardapio.getNome() != null) {
                        existingCardapio.setNome(cardapio.getNome());
                    }

                    return existingCardapio;
                }
            )
            .map(cardapioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardapio.getId().toString())
        );
    }

    /**
     * {@code GET  /cardapios} : get all the cardapios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardapios in body.
     */
    @GetMapping("/cardapios")
    public List<Cardapio> getAllCardapios() {
        log.debug("REST request to get all Cardapios");
        return cardapioRepository.findAll();
    }

    /**
     * {@code GET  /cardapios/:id} : get the "id" cardapio.
     *
     * @param id the id of the cardapio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardapio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cardapios/{id}")
    public ResponseEntity<Cardapio> getCardapio(@PathVariable Long id) {
        log.debug("REST request to get Cardapio : {}", id);
        Optional<Cardapio> cardapio = cardapioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cardapio);
    }

    /**
     * {@code DELETE  /cardapios/:id} : delete the "id" cardapio.
     *
     * @param id the id of the cardapio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cardapios/{id}")
    public ResponseEntity<Void> deleteCardapio(@PathVariable Long id) {
        log.debug("REST request to delete Cardapio : {}", id);
        cardapioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
