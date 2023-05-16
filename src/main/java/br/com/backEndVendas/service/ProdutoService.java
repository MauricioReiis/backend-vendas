package br.com.backEndVendas.service;

import br.com.backEndVendas.service.dto.CompraBuscarProdutoDto;
import br.com.backEndVendas.service.dto.CompraProdutoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProdutoService {

    @Qualifier("mock")
    @Autowired
    RestTemplate rest;

    public CompraBuscarProdutoDto produtoPeloId(int idProduto) {
        String url = "https://localhost:8080/compras/buscar/produto/"+idProduto;
        ResponseEntity<CompraBuscarProdutoDto> resp = rest.getForEntity(url, CompraBuscarProdutoDto.class);

        return resp.getBody();
    }
    public boolean validarProdutoExistente(int idProduto) {
        String url = "https://localhost:8080/compras/validar/status/produto/"+idProduto;
        ResponseEntity<CompraProdutoDto> resp = rest.getForEntity(url, CompraProdutoDto.class);
        CompraProdutoDto c = resp.getBody();
        boolean result = c.isProdutoExistente();
        return result;
    }
    public boolean validarProdutoEstoque( int idProduto, int quantidade) {
        String url = "https://localhost:8080/compras/validar/status/produto/"+idProduto;
        ResponseEntity<CompraProdutoDto> resp = rest.getForEntity(url, CompraProdutoDto.class);
        CompraProdutoDto c = resp.getBody();
        int result = c.getQtdEstoque();
        return result >= quantidade;
    }

    public int verificarEstoqueDisponível(int idProduto, int quantidade) throws JsonProcessingException {

            if (!validarProdutoExistente(idProduto)){
                return 1;
            }

            if (validarProdutoEstoque( idProduto,quantidade)) {

                return 2;
            }


        return 3;
    }

}
