package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoDao extends JpaRepository<Produto, Integer> {
}
