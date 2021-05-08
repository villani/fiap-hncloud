package br.com.qrdapio.web.rest;

import static br.com.qrdapio.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.qrdapio.IntegrationTest;
import br.com.qrdapio.domain.Cardapio;
import br.com.qrdapio.domain.ItemCardapio;
import br.com.qrdapio.domain.enumeration.Categoria;
import br.com.qrdapio.repository.ItemCardapioRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ItemCardapioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemCardapioResourceIT {

    private static final Categoria DEFAULT_CATEGORIA = Categoria.PRATO;
    private static final Categoria UPDATED_CATEGORIA = Categoria.BEBIDA;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/item-cardapios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemCardapioMockMvc;

    private ItemCardapio itemCardapio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCardapio createEntity(EntityManager em) {
        ItemCardapio itemCardapio = new ItemCardapio()
            .categoria(DEFAULT_CATEGORIA)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR);
        // Add required entity
        Cardapio cardapio;
        if (TestUtil.findAll(em, Cardapio.class).isEmpty()) {
            cardapio = CardapioResourceIT.createEntity(em);
            em.persist(cardapio);
            em.flush();
        } else {
            cardapio = TestUtil.findAll(em, Cardapio.class).get(0);
        }
        itemCardapio.setCardapio(cardapio);
        return itemCardapio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemCardapio createUpdatedEntity(EntityManager em) {
        ItemCardapio itemCardapio = new ItemCardapio()
            .categoria(UPDATED_CATEGORIA)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR);
        // Add required entity
        Cardapio cardapio;
        if (TestUtil.findAll(em, Cardapio.class).isEmpty()) {
            cardapio = CardapioResourceIT.createUpdatedEntity(em);
            em.persist(cardapio);
            em.flush();
        } else {
            cardapio = TestUtil.findAll(em, Cardapio.class).get(0);
        }
        itemCardapio.setCardapio(cardapio);
        return itemCardapio;
    }

    @BeforeEach
    public void initTest() {
        itemCardapio = createEntity(em);
    }

    @Test
    @Transactional
    void createItemCardapio() throws Exception {
        int databaseSizeBeforeCreate = itemCardapioRepository.findAll().size();
        // Create the ItemCardapio
        restItemCardapioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isCreated());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeCreate + 1);
        ItemCardapio testItemCardapio = itemCardapioList.get(itemCardapioList.size() - 1);
        assertThat(testItemCardapio.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testItemCardapio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testItemCardapio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testItemCardapio.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void createItemCardapioWithExistingId() throws Exception {
        // Create the ItemCardapio with an existing ID
        itemCardapio.setId(1L);

        int databaseSizeBeforeCreate = itemCardapioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemCardapioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCardapioRepository.findAll().size();
        // set the field null
        itemCardapio.setCategoria(null);

        // Create the ItemCardapio, which fails.

        restItemCardapioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCardapioRepository.findAll().size();
        // set the field null
        itemCardapio.setNome(null);

        // Create the ItemCardapio, which fails.

        restItemCardapioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCardapioRepository.findAll().size();
        // set the field null
        itemCardapio.setDescricao(null);

        // Create the ItemCardapio, which fails.

        restItemCardapioMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemCardapios() throws Exception {
        // Initialize the database
        itemCardapioRepository.saveAndFlush(itemCardapio);

        // Get all the itemCardapioList
        restItemCardapioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCardapio.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))));
    }

    @Test
    @Transactional
    void getItemCardapio() throws Exception {
        // Initialize the database
        itemCardapioRepository.saveAndFlush(itemCardapio);

        // Get the itemCardapio
        restItemCardapioMockMvc
            .perform(get(ENTITY_API_URL_ID, itemCardapio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemCardapio.getId().intValue()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)));
    }

    @Test
    @Transactional
    void getNonExistingItemCardapio() throws Exception {
        // Get the itemCardapio
        restItemCardapioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemCardapio() throws Exception {
        // Initialize the database
        itemCardapioRepository.saveAndFlush(itemCardapio);

        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();

        // Update the itemCardapio
        ItemCardapio updatedItemCardapio = itemCardapioRepository.findById(itemCardapio.getId()).get();
        // Disconnect from session so that the updates on updatedItemCardapio are not directly saved in db
        em.detach(updatedItemCardapio);
        updatedItemCardapio.categoria(UPDATED_CATEGORIA).nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).valor(UPDATED_VALOR);

        restItemCardapioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemCardapio.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemCardapio))
            )
            .andExpect(status().isOk());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
        ItemCardapio testItemCardapio = itemCardapioList.get(itemCardapioList.size() - 1);
        assertThat(testItemCardapio.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testItemCardapio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testItemCardapio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testItemCardapio.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void putNonExistingItemCardapio() throws Exception {
        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();
        itemCardapio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCardapioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemCardapio.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemCardapio() throws Exception {
        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();
        itemCardapio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCardapioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemCardapio() throws Exception {
        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();
        itemCardapio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCardapioMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemCardapioWithPatch() throws Exception {
        // Initialize the database
        itemCardapioRepository.saveAndFlush(itemCardapio);

        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();

        // Update the itemCardapio using partial update
        ItemCardapio partialUpdatedItemCardapio = new ItemCardapio();
        partialUpdatedItemCardapio.setId(itemCardapio.getId());

        restItemCardapioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemCardapio.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemCardapio))
            )
            .andExpect(status().isOk());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
        ItemCardapio testItemCardapio = itemCardapioList.get(itemCardapioList.size() - 1);
        assertThat(testItemCardapio.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);
        assertThat(testItemCardapio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testItemCardapio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testItemCardapio.getValor()).isEqualByComparingTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void fullUpdateItemCardapioWithPatch() throws Exception {
        // Initialize the database
        itemCardapioRepository.saveAndFlush(itemCardapio);

        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();

        // Update the itemCardapio using partial update
        ItemCardapio partialUpdatedItemCardapio = new ItemCardapio();
        partialUpdatedItemCardapio.setId(itemCardapio.getId());

        partialUpdatedItemCardapio.categoria(UPDATED_CATEGORIA).nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).valor(UPDATED_VALOR);

        restItemCardapioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemCardapio.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemCardapio))
            )
            .andExpect(status().isOk());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
        ItemCardapio testItemCardapio = itemCardapioList.get(itemCardapioList.size() - 1);
        assertThat(testItemCardapio.getCategoria()).isEqualTo(UPDATED_CATEGORIA);
        assertThat(testItemCardapio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testItemCardapio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testItemCardapio.getValor()).isEqualByComparingTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void patchNonExistingItemCardapio() throws Exception {
        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();
        itemCardapio.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemCardapioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemCardapio.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemCardapio() throws Exception {
        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();
        itemCardapio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCardapioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemCardapio() throws Exception {
        int databaseSizeBeforeUpdate = itemCardapioRepository.findAll().size();
        itemCardapio.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemCardapioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemCardapio))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemCardapio in the database
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemCardapio() throws Exception {
        // Initialize the database
        itemCardapioRepository.saveAndFlush(itemCardapio);

        int databaseSizeBeforeDelete = itemCardapioRepository.findAll().size();

        // Delete the itemCardapio
        restItemCardapioMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemCardapio.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemCardapio> itemCardapioList = itemCardapioRepository.findAll();
        assertThat(itemCardapioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
