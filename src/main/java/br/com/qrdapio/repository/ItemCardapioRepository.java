package br.com.qrdapio.repository;

import br.com.qrdapio.domain.ItemCardapio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemCardapio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {}
