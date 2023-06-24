package br.com.backEndVendas.mock;


import br.com.backEndVendas.service.dto.ClienteCadastroDto;

import br.com.backEndVendas.service.dto.ClienteStatusDto;
import br.com.backEndVendas.service.dto.CompraBuscarProdutoDto;
import br.com.backEndVendas.service.dto.CompraCarrinhoDto;
import br.com.backEndVendas.service.dto.CompraProdutoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component(value = "mock")
public class MockRestTemplate extends RestTemplate {
    static Map<String, Object> comandos;

    static {
        comandos = new HashMap<>();

        comandos.put("https://localhost:8080/compras/validar/pagamento/1/123456/json/",
                CompraCarrinhoDto.builder()
                        .idCarrinho(123456)
                        .pgAprovado(true)
                        .idCliente(1)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/pagamento/5/123456789/json/",
                CompraCarrinhoDto.builder()
                        .idCarrinho(123456789)
                        .pgAprovado(false)
                        .idCliente(5)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/status/produto/2",
                CompraProdutoDto.builder()
                        .qtdEstoque(20)
                        .produtoExistente(true)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/status/produto/3",
                CompraProdutoDto.builder()
                        .qtdEstoque(30)
                        .produtoExistente(true)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/status/produto/4",
                CompraProdutoDto.builder()
                        .qtdEstoque(40)
                        .produtoExistente(true)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/status/produto/5",
                CompraProdutoDto.builder()
                        .qtdEstoque(40)
                        .produtoExistente(false)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/status/produto/6",
                CompraProdutoDto.builder()
                        .qtdEstoque(40)
                        .produtoExistente(false)
                        .build()
        );

        comandos.put("https://localhost:8080/compras/buscar/produto/2",
                CompraBuscarProdutoDto.builder()
                        .qtdEstoque(40)
                        .nomeProduto("pneu")
                        .idProduto(2)
                        .qtdEstoque(20)
                        .precoUnit(100)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/buscar/produto/3",
                CompraBuscarProdutoDto.builder()
                        .qtdEstoque(30)
                        .nomeProduto("mesa")
                        .idProduto(3)
                        .qtdEstoque(20)
                        .precoUnit(500)
                        .build()
        );
        comandos.put("https://localhost:8080/crm/buscar/cliente/1",
                ClienteStatusDto.builder()
                        .nome("Rafael")
                        .telefone("32988983168")
                        .build()
        );

        comandos.put("https://localhost:8080/crm/cliente/verificarCadastro/1",
                ClienteCadastroDto.builder()
                        .cadastro(true)
                        .build()
        );

        comandos.put("https://localhost:8080/crm/cliente/verificarCadastro/2",
                ClienteCadastroDto.builder()
                        .cadastro(false)
                        .build()
        );

    }
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> reponseType, Object ...uriVariables){

        return  new ResponseEntity<T>((T) comandos.get(url), HttpStatus.OK);
    }
}
