package br.com.qrdapio.repository;

import br.com.qrdapio.domain.Cardapio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cardapio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Long> {}
