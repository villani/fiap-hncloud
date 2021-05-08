package br.com.qrdapio.web.rest;

import br.com.qrdapio.domain.ItemPedido;
import br.com.qrdapio.repository.ItemPedidoRepository;
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
 * REST controller for managing {@link br.com.qrdapio.domain.ItemPedido}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ItemPedidoResource {

    private final Logger log = LoggerFactory.getLogger(ItemPedidoResource.class);

    private static final String ENTITY_NAME = "itemPedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoResource(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    /**
     * {@code POST  /item-pedidos} : Create a new itemPedido.
     *
     * @param itemPedido the itemPedido to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemPedido, or with status {@code 400 (Bad Request)} if the itemPedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-pedidos")
    public ResponseEntity<ItemPedido> createItemPedido(@Valid @RequestBody ItemPedido itemPedido) throws URISyntaxException {
        log.debug("REST request to save ItemPedido : {}", itemPedido);
        if (itemPedido.getId() != null) {
            throw new BadRequestAlertException("A new itemPedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemPedido result = itemPedidoRepository.save(itemPedido);
        return ResponseEntity
            .created(new URI("/api/item-pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-pedidos/:id} : Updates an existing itemPedido.
     *
     * @param id the id of the itemPedido to save.
     * @param itemPedido the itemPedido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemPedido,
     * or with status {@code 400 (Bad Request)} if the itemPedido is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemPedido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-pedidos/{id}")
    public ResponseEntity<ItemPedido> updateItemPedido(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemPedido itemPedido
    ) throws URISyntaxException {
        log.debug("REST request to update ItemPedido : {}, {}", id, itemPedido);
        if (itemPedido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemPedido.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemPedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemPedido result = itemPedidoRepository.save(itemPedido);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemPedido.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-pedidos/:id} : Partial updates given fields of an existing itemPedido, field will ignore if it is null
     *
     * @param id the id of the itemPedido to save.
     * @param itemPedido the itemPedido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemPedido,
     * or with status {@code 400 (Bad Request)} if the itemPedido is not valid,
     * or with status {@code 404 (Not Found)} if the itemPedido is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemPedido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-pedidos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ItemPedido> partialUpdateItemPedido(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemPedido itemPedido
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemPedido partially : {}, {}", id, itemPedido);
        if (itemPedido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemPedido.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemPedidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemPedido> result = itemPedidoRepository
            .findById(itemPedido.getId())
            .map(
                existingItemPedido -> {
                    if (itemPedido.getQuantidade() != null) {
                        existingItemPedido.setQuantidade(itemPedido.getQuantidade());
                    }

                    return existingItemPedido;
                }
            )
            .map(itemPedidoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemPedido.getId().toString())
        );
    }

    /**
     * {@code GET  /item-pedidos} : get all the itemPedidos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemPedidos in body.
     */
    @GetMapping("/item-pedidos")
    public List<ItemPedido> getAllItemPedidos() {
        log.debug("REST request to get all ItemPedidos");
        return itemPedidoRepository.findAll();
    }

    /**
     * {@code GET  /item-pedidos/:id} : get the "id" itemPedido.
     *
     * @param id the id of the itemPedido to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemPedido, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-pedidos/{id}")
    public ResponseEntity<ItemPedido> getItemPedido(@PathVariable Long id) {
        log.debug("REST request to get ItemPedido : {}", id);
        Optional<ItemPedido> itemPedido = itemPedidoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemPedido);
    }

    /**
     * {@code DELETE  /item-pedidos/:id} : delete the "id" itemPedido.
     *
     * @param id the id of the itemPedido to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-pedidos/{id}")
    public ResponseEntity<Void> deleteItemPedido(@PathVariable Long id) {
        log.debug("REST request to delete ItemPedido : {}", id);
        itemPedidoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
