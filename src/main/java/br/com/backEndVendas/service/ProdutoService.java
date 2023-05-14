package br.com.backEndVendas.service;

import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.dao.ProdutoDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public  boolean verificarProdutoExistente(int id){
        Optional<Produto> op = prodao.findById(id);
        if (op.isPresent()){
            return  true;
        }else {
            return false;
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
    public int verificarEstoqueDisponível(String produtoJson, int quantidade) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(produtoJson);

        JsonNode qtdEstoqueNode = jsonNode.get("qtdEstoque");
        JsonNode idProdutoNode = jsonNode.get("idProduto");

        if (qtdEstoqueNode != null && idProdutoNode != null) {

            int idProduto = idProdutoNode.asInt();

            if (!verificarProdutoExistente(idProduto)){
                return 1;
            }

            Produto p = buscarProdutoPeloId(idProduto);

            if (p.getQtdEstoque() >= quantidade) {
                p.setQtdEstoque(p.getQtdEstoque() - quantidade);
                prodao.save(p);
                return 2;
            }
        }

        return 3;
    }

}
