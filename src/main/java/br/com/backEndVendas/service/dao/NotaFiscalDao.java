package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaFiscalDao extends JpaRepository<NotaFiscal, Integer> {
}
