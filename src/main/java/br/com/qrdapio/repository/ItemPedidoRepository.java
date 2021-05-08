package br.com.qrdapio.repository;

import br.com.qrdapio.domain.ItemPedido;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemPedido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {}
