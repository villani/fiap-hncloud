package br.com.qrdapio.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.qrdapio.IntegrationTest;
import br.com.qrdapio.domain.ItemCardapio;
import br.com.qrdapio.domain.ItemPedido;
import br.com.qrdapio.domain.Pedido;
import br.com.qrdapio.repository.ItemPedidoRepository;
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
 * Integration tests for the {@link ItemPedidoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemPedidoResourceIT {

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    private static final String ENTITY_API_URL = "/api/item-pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemPedidoMockMvc;

    private ItemPedido itemPedido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemPedido createEntity(EntityManager em) {
        ItemPedido itemPedido = new ItemPedido().quantidade(DEFAULT_QUANTIDADE);
        // Add required entity
        ItemCardapio itemCardapio;
        if (TestUtil.findAll(em, ItemCardapio.class).isEmpty()) {
            itemCardapio = ItemCardapioResourceIT.createEntity(em);
            em.persist(itemCardapio);
            em.flush();
        } else {
            itemCardapio = TestUtil.findAll(em, ItemCardapio.class).get(0);
        }
        itemPedido.setItem(itemCardapio);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).get(0);
        }
        itemPedido.setPedido(pedido);
        return itemPedido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemPedido createUpdatedEntity(EntityManager em) {
        ItemPedido itemPedido = new ItemPedido().quantidade(UPDATED_QUANTIDADE);
        // Add required entity
        ItemCardapio itemCardapio;
        if (TestUtil.findAll(em, ItemCardapio.class).isEmpty()) {
            itemCardapio = ItemCardapioResourceIT.createUpdatedEntity(em);
            em.persist(itemCardapio);
            em.flush();
        } else {
            itemCardapio = TestUtil.findAll(em, ItemCardapio.class).get(0);
        }
        itemPedido.setItem(itemCardapio);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createUpdatedEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).get(0);
        }
        itemPedido.setPedido(pedido);
        return itemPedido;
    }

    @BeforeEach
    public void initTest() {
        itemPedido = createEntity(em);
    }

    @Test
    @Transactional
    void createItemPedido() throws Exception {
        int databaseSizeBeforeCreate = itemPedidoRepository.findAll().size();
        // Create the ItemPedido
        restItemPedidoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isCreated());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeCreate + 1);
        ItemPedido testItemPedido = itemPedidoList.get(itemPedidoList.size() - 1);
        assertThat(testItemPedido.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
    }

    @Test
    @Transactional
    void createItemPedidoWithExistingId() throws Exception {
        // Create the ItemPedido with an existing ID
        itemPedido.setId(1L);

        int databaseSizeBeforeCreate = itemPedidoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemPedidoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemPedidos() throws Exception {
        // Initialize the database
        itemPedidoRepository.saveAndFlush(itemPedido);

        // Get all the itemPedidoList
        restItemPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemPedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)));
    }

    @Test
    @Transactional
    void getItemPedido() throws Exception {
        // Initialize the database
        itemPedidoRepository.saveAndFlush(itemPedido);

        // Get the itemPedido
        restItemPedidoMockMvc
            .perform(get(ENTITY_API_URL_ID, itemPedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemPedido.getId().intValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE));
    }

    @Test
    @Transactional
    void getNonExistingItemPedido() throws Exception {
        // Get the itemPedido
        restItemPedidoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemPedido() throws Exception {
        // Initialize the database
        itemPedidoRepository.saveAndFlush(itemPedido);

        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();

        // Update the itemPedido
        ItemPedido updatedItemPedido = itemPedidoRepository.findById(itemPedido.getId()).get();
        // Disconnect from session so that the updates on updatedItemPedido are not directly saved in db
        em.detach(updatedItemPedido);
        updatedItemPedido.quantidade(UPDATED_QUANTIDADE);

        restItemPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemPedido.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemPedido))
            )
            .andExpect(status().isOk());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
        ItemPedido testItemPedido = itemPedidoList.get(itemPedidoList.size() - 1);
        assertThat(testItemPedido.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void putNonExistingItemPedido() throws Exception {
        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();
        itemPedido.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemPedido.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemPedido() throws Exception {
        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();
        itemPedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemPedido() throws Exception {
        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();
        itemPedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemPedidoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemPedidoWithPatch() throws Exception {
        // Initialize the database
        itemPedidoRepository.saveAndFlush(itemPedido);

        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();

        // Update the itemPedido using partial update
        ItemPedido partialUpdatedItemPedido = new ItemPedido();
        partialUpdatedItemPedido.setId(itemPedido.getId());

        partialUpdatedItemPedido.quantidade(UPDATED_QUANTIDADE);

        restItemPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemPedido.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemPedido))
            )
            .andExpect(status().isOk());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
        ItemPedido testItemPedido = itemPedidoList.get(itemPedidoList.size() - 1);
        assertThat(testItemPedido.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void fullUpdateItemPedidoWithPatch() throws Exception {
        // Initialize the database
        itemPedidoRepository.saveAndFlush(itemPedido);

        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();

        // Update the itemPedido using partial update
        ItemPedido partialUpdatedItemPedido = new ItemPedido();
        partialUpdatedItemPedido.setId(itemPedido.getId());

        partialUpdatedItemPedido.quantidade(UPDATED_QUANTIDADE);

        restItemPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemPedido.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemPedido))
            )
            .andExpect(status().isOk());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
        ItemPedido testItemPedido = itemPedidoList.get(itemPedidoList.size() - 1);
        assertThat(testItemPedido.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void patchNonExistingItemPedido() throws Exception {
        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();
        itemPedido.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemPedido.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemPedido() throws Exception {
        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();
        itemPedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemPedido() throws Exception {
        int databaseSizeBeforeUpdate = itemPedidoRepository.findAll().size();
        itemPedido.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemPedido))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemPedido in the database
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemPedido() throws Exception {
        // Initialize the database
        itemPedidoRepository.saveAndFlush(itemPedido);

        int databaseSizeBeforeDelete = itemPedidoRepository.findAll().size();

        // Delete the itemPedido
        restItemPedidoMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemPedido.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemPedido> itemPedidoList = itemPedidoRepository.findAll();
        assertThat(itemPedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
