package br.com.backEndVendas.service;

import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.dao.ProdutoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoService {

    @Autowired
    ProdutoDao prodao;

    public Produto save(Produto produto){
        return prodao.save(produto);
    }
}
