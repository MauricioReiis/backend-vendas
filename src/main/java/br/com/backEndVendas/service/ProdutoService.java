package br.com.backEndVendas.service;

import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.dao.ProdutoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ProdutoService {

    @Autowired
    ProdutoDao prodao;

    public Produto save(Produto produto){
        return prodao.save(produto);
    }

    public Produto updateProduto(int id, Produto produto) throws Exception {
        Produto p = buscarProdutoPeloId(id);
        if (p == null){
            throw new Exception("Produto não encontrado");
        }
        p.setIdProduto(produto.getIdProduto());
        p.setNomeProduto(produto.getNomeProduto());
        p.setQtdEstoque(produto.getQtdEstoque());
        p.setPrecoUnit(produto.getPrecoUnit());

        return  prodao.save(p);

    }

    public  Produto buscarProdutoPeloId(int id){
        Optional<Produto> op = prodao.findById(id);
        if (op.isPresent()){
            return  op.get();
        }else {
            return null;
        }
    }

    public String cancelarProduto(int id) throws Exception {
        Produto p = buscarProdutoPeloId(id);
        if (p == null){
            throw new Exception("Produto não encontrado");
        }
        prodao.delete(p);
        return "Produto cancelado com sucesso!";
    }
}
