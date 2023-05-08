package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoDao extends JpaRepository<ItemPedido, Long> {
}
