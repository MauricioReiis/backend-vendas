package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.DevolucaoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevolucaoDao extends JpaRepository<DevolucaoPedido, Integer> {
}

