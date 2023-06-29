package br.com.backEndVendas.service;

import br.com.backEndVendas.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
public class ProdutoService {

    @Autowired
    RestTemplate rest;

    public CompraBuscarProdutoDto produtoPeloId(int idProduto) {
        String url = "https://gateway-sgeu.up.railway.app/compras/produto/" + idProduto;
        ResponseEntity<CompraBuscarProdutoDto> resp = rest.getForEntity(url, CompraBuscarProdutoDto.class);

        return resp.getBody();
    }

    public boolean validarProdutoExistente(int idProduto) {
        String url = "https://gateway-sgeu.up.railway.app/compras/produto/verificar/" + idProduto;
        ResponseEntity<CompraProdutoDto> resp = rest.getForEntity(url, CompraProdutoDto.class);
        CompraProdutoDto c = resp.getBody();
        boolean result = c.isProdutoExistente();
        return result;
    }

    public boolean verificarEstoque(int idProduto, int qtdeProduto) {
        String url = "https://gateway-sgeu.up.railway.app/compras/produto/verificar/" + idProduto;
        ResponseEntity<EstoqueResponseDto> resp = rest.getForEntity(url, EstoqueResponseDto.class);
        EstoqueResponseDto estoqueResponse = resp.getBody();

        return estoqueResponse != null && estoqueResponse.isProdutoExistente() && estoqueResponse.getQtdEstoque() >= qtdeProduto;
    }

    public Boolean atualizarEstoque(int idProduto, int qnt) {
        String url = "https://gateway-sgeu.up.railway.app/compras/estoque/debitar";

        try {
            AtualizarEstoqueDto atualizarEstoqueDto = new AtualizarEstoqueDto(idProduto, qnt);
            HttpEntity<AtualizarEstoqueDto> requestEntity = new HttpEntity<>(atualizarEstoqueDto);
            ResponseEntity<AtualizarEstoqueDto> response = rest.exchange(url, HttpMethod.POST, requestEntity, AtualizarEstoqueDto.class);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void verificarPrazoDevolucao(LocalDate dataDevolucao, int diasExpiracao) throws Exception {
        LocalDate dataAtual = LocalDate.now();
        long diferencaDias = (ChronoUnit.DAYS.between(dataDevolucao, dataAtual) * -1);

        if (diferencaDias <= diasExpiracao) {
        } else {
            throw new Exception("O prazo para devolução expirou.");
        }
    }

}
