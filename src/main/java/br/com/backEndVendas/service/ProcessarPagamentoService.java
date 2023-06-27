package br.com.backEndVendas.service;

import br.com.backEndVendas.service.dto.PagamentosCarrinhoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
