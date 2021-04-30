package br.com.qrdapio.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.qrdapio.IntegrationTest;
import br.com.qrdapio.domain.Restaurante;
import br.com.qrdapio.repository.RestauranteRepository;
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
 * Integration tests for the {@link RestauranteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestauranteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestauranteMockMvc;

    private Restaurante restaurante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurante createEntity(EntityManager em) {
        Restaurante restaurante = new Restaurante().nome(DEFAULT_NOME);
        return restaurante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurante createUpdatedEntity(EntityManager em) {
        Restaurante restaurante = new Restaurante().nome(UPDATED_NOME);
        return restaurante;
    }

    @BeforeEach
    public void initTest() {
        restaurante = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurante() throws Exception {
        int databaseSizeBeforeCreate = restauranteRepository.findAll().size();
        // Create the Restaurante
        restRestauranteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isCreated());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createRestauranteWithExistingId() throws Exception {
        // Create the Restaurante with an existing ID
        restaurante.setId(1L);

        int databaseSizeBeforeCreate = restauranteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestauranteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = restauranteRepository.findAll().size();
        // set the field null
        restaurante.setNome(null);

        // Create the Restaurante, which fails.

        restRestauranteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isBadRequest());

        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestaurantes() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        // Get all the restauranteList
        restRestauranteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        // Get the restaurante
        restRestauranteMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurante.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingRestaurante() throws Exception {
        // Get the restaurante
        restRestauranteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Update the restaurante
        Restaurante updatedRestaurante = restauranteRepository.findById(restaurante.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurante are not directly saved in db
        em.detach(updatedRestaurante);
        updatedRestaurante.nome(UPDATED_NOME);

        restRestauranteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurante.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRestaurante))
            )
            .andExpect(status().isOk());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();
        restaurante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestauranteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurante.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();
        restaurante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestauranteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();
        restaurante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestauranteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestauranteWithPatch() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Update the restaurante using partial update
        Restaurante partialUpdatedRestaurante = new Restaurante();
        partialUpdatedRestaurante.setId(restaurante.getId());

        partialUpdatedRestaurante.nome(UPDATED_NOME);

        restRestauranteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurante.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurante))
            )
            .andExpect(status().isOk());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateRestauranteWithPatch() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();

        // Update the restaurante using partial update
        Restaurante partialUpdatedRestaurante = new Restaurante();
        partialUpdatedRestaurante.setId(restaurante.getId());

        partialUpdatedRestaurante.nome(UPDATED_NOME);

        restRestauranteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurante.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurante))
            )
            .andExpect(status().isOk());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
        Restaurante testRestaurante = restauranteList.get(restauranteList.size() - 1);
        assertThat(testRestaurante.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();
        restaurante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestauranteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurante.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();
        restaurante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestauranteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurante() throws Exception {
        int databaseSizeBeforeUpdate = restauranteRepository.findAll().size();
        restaurante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestauranteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurante in the database
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurante() throws Exception {
        // Initialize the database
        restauranteRepository.saveAndFlush(restaurante);

        int databaseSizeBeforeDelete = restauranteRepository.findAll().size();

        // Delete the restaurante
        restRestauranteMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurante.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurante> restauranteList = restauranteRepository.findAll();
        assertThat(restauranteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
