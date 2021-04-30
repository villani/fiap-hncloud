package br.com.qrdapio.web.rest;

import br.com.qrdapio.domain.ItemCardapio;
import br.com.qrdapio.repository.ItemCardapioRepository;
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
 * REST controller for managing {@link br.com.qrdapio.domain.ItemCardapio}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ItemCardapioResource {

    private final Logger log = LoggerFactory.getLogger(ItemCardapioResource.class);

    private static final String ENTITY_NAME = "itemCardapio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemCardapioRepository itemCardapioRepository;

    public ItemCardapioResource(ItemCardapioRepository itemCardapioRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
    }

    /**
     * {@code POST  /item-cardapios} : Create a new itemCardapio.
     *
     * @param itemCardapio the itemCardapio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemCardapio, or with status {@code 400 (Bad Request)} if the itemCardapio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-cardapios")
    public ResponseEntity<ItemCardapio> createItemCardapio(@Valid @RequestBody ItemCardapio itemCardapio) throws URISyntaxException {
        log.debug("REST request to save ItemCardapio : {}", itemCardapio);
        if (itemCardapio.getId() != null) {
            throw new BadRequestAlertException("A new itemCardapio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemCardapio result = itemCardapioRepository.save(itemCardapio);
        return ResponseEntity
            .created(new URI("/api/item-cardapios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-cardapios/:id} : Updates an existing itemCardapio.
     *
     * @param id the id of the itemCardapio to save.
     * @param itemCardapio the itemCardapio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCardapio,
     * or with status {@code 400 (Bad Request)} if the itemCardapio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemCardapio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-cardapios/{id}")
    public ResponseEntity<ItemCardapio> updateItemCardapio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemCardapio itemCardapio
    ) throws URISyntaxException {
        log.debug("REST request to update ItemCardapio : {}, {}", id, itemCardapio);
        if (itemCardapio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCardapio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemCardapioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemCardapio result = itemCardapioRepository.save(itemCardapio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemCardapio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-cardapios/:id} : Partial updates given fields of an existing itemCardapio, field will ignore if it is null
     *
     * @param id the id of the itemCardapio to save.
     * @param itemCardapio the itemCardapio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemCardapio,
     * or with status {@code 400 (Bad Request)} if the itemCardapio is not valid,
     * or with status {@code 404 (Not Found)} if the itemCardapio is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemCardapio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-cardapios/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemCardapio> partialUpdateItemCardapio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemCardapio itemCardapio
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemCardapio partially : {}, {}", id, itemCardapio);
        if (itemCardapio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemCardapio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemCardapioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemCardapio> result = itemCardapioRepository
            .findById(itemCardapio.getId())
            .map(
                existingItemCardapio -> {
                    if (itemCardapio.getCategoria() != null) {
                        existingItemCardapio.setCategoria(itemCardapio.getCategoria());
                    }
                    if (itemCardapio.getNome() != null) {
                        existingItemCardapio.setNome(itemCardapio.getNome());
                    }
                    if (itemCardapio.getDescricao() != null) {
                        existingItemCardapio.setDescricao(itemCardapio.getDescricao());
                    }
                    if (itemCardapio.getValor() != null) {
                        existingItemCardapio.setValor(itemCardapio.getValor());
                    }

                    return existingItemCardapio;
                }
            )
            .map(itemCardapioRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemCardapio.getId().toString())
        );
    }

    /**
     * {@code GET  /item-cardapios} : get all the itemCardapios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemCardapios in body.
     */
    @GetMapping("/item-cardapios")
    public List<ItemCardapio> getAllItemCardapios() {
        log.debug("REST request to get all ItemCardapios");
        return itemCardapioRepository.findAll();
    }

    /**
     * {@code GET  /item-cardapios/:id} : get the "id" itemCardapio.
     *
     * @param id the id of the itemCardapio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemCardapio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-cardapios/{id}")
    public ResponseEntity<ItemCardapio> getItemCardapio(@PathVariable Long id) {
        log.debug("REST request to get ItemCardapio : {}", id);
        Optional<ItemCardapio> itemCardapio = itemCardapioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemCardapio);
    }

    /**
     * {@code DELETE  /item-cardapios/:id} : delete the "id" itemCardapio.
     *
     * @param id the id of the itemCardapio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-cardapios/{id}")
    public ResponseEntity<Void> deleteItemCardapio(@PathVariable Long id) {
        log.debug("REST request to delete ItemCardapio : {}", id);
        itemCardapioRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
