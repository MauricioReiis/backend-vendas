package br.com.backEndVendas.mock;

import br.com.backEndVendas.service.dto.CompraCarrinhoDto;
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
                        .idCarrinho(1234)
                        .pgAprovado(true)
                        .idCliente(1)
                        .build()
        );
        comandos.put("https://localhost:8080/compras/validar/pagamento/5/123456789/json/",
                CompraCarrinhoDto.builder()
                        .idCarrinho(12345)
                        .pgAprovado(false)
                        .idCliente(2)
                        .build()
        );
    }
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> reponseType, Object ...uriVariables){

        return  new ResponseEntity<T>((T) comandos.get(url), HttpStatus.OK);
    }
}
