package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.NotaVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaVendaDao extends JpaRepository<NotaVenda, Integer> {
}
