package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/produtos")
@RestController
public class ProdutoResource {

    @Autowired
    ProdutoService prodServ;

    @PostMapping
    public Produto adicionarPedido(@RequestBody Produto produto){
        return prodServ.save(produto);
    }
}
