package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoDao extends JpaRepository<Pedido, Integer> {

}
