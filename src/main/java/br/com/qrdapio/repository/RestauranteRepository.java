package br.com.qrdapio.repository;

import br.com.qrdapio.domain.Restaurante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {}
