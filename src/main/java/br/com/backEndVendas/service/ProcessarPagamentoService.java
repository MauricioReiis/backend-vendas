package br.com.backEndVendas.service;

import br.com.backEndVendas.service.dto.PagamentosCarrinhoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class ProcessarPagamentoService {

    @Autowired
    private RestTemplate rest;

    public boolean realizarPagamento(int clientId, int carrinhoId, double valorTotal, String formaPagamento) throws JsonProcessingException {
        String url = "https://modulo-pagamento-production.up.railway.app/modulo-de-pagamentos/carrinho";
        PagamentosCarrinhoDto pagamentoCarrinhoDto = new PagamentosCarrinhoDto();
        pagamentoCarrinhoDto.setClientId(clientId);
        pagamentoCarrinhoDto.setCarrinhoId(carrinhoId);
        pagamentoCarrinhoDto.setValorTotal(valorTotal);
        pagamentoCarrinhoDto.setFormaPagamento(formaPagamento);

        HttpEntity<PagamentosCarrinhoDto> requestEntity = new HttpEntity<>(pagamentoCarrinhoDto);
        ResponseEntity<Boolean> response = rest.exchange(url, HttpMethod.POST, requestEntity, Boolean.class);

        System.out.println(response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            Boolean isValid  = response.getBody();
            return true;
        } else {
            return false;
        }
    }
}
