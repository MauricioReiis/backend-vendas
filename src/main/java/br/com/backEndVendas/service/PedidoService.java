package br.com.backEndVendas.service;

import br.com.backEndVendas.model.*;
import br.com.backEndVendas.service.dao.ItemPedidoDao;
import br.com.backEndVendas.service.dao.NotaVendaDao;
import br.com.backEndVendas.service.dao.PedidoDao;
import br.com.backEndVendas.service.dao.ProdutoDao;
import br.com.backEndVendas.service.dto.CompraBuscarProdutoDto;
import br.com.backEndVendas.service.dto.CompraCarrinhoDto;
import br.com.backEndVendas.service.dto.EnderecoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Service
public class PedidoService {

    @Autowired
    PedidoDao pdao;
    @Autowired
    NotaVendaDao notaVendaDao;
    @Autowired
    ItemPedidoDao itemPedidoDao;
    @Autowired
    ProdutoDao produtoDao;
    @Autowired
    ProdutoService produtoService;
    @Qualifier("mock")
    @Autowired
    RestTemplate rest;

    @Autowired
    FretService fretService;



    public Pedido buscarPedidoPeloId(int id) {
        Optional<Pedido> op = pdao.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            return null;
        }
    }

    public String formatarResposta(String resposta) {
        return "{\"status\": \"" + resposta + "\"}";
    }

    public String realizarPedido(String pedidoJson) throws Exception {
        int pedidoResponse = processarPedido(pedidoJson);

        switch (pedidoResponse) {
            case 1:
                throw new Exception("Id de produto inexistente!");
            case 2:
                throw new Exception("Quantidade de produto indisponível!");
            case 3:
                return "Pedido Realizado com sucesso!";
            case 4:
                throw new Exception("Houve um erro com o pagamento do pedido!");

            default:
                throw new Exception("Erro inesperado!");
        }
    }

    public boolean validarCarrinho(int idCarrinho, int idCliente) {
        String url = "https://localhost:8080/compras/validar/pagamento/" + idCliente + "/" + idCarrinho + "/json/";
        ResponseEntity<CompraCarrinhoDto> resp = rest.getForEntity(url, CompraCarrinhoDto.class);
        CompraCarrinhoDto c = resp.getBody();
        boolean result = c.isPgAprovado();
        return result;
    }


    public int processarPedido(String pedidoJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(pedidoJson);

        double precoTotal = jsonNode.get("precoTotal").asDouble();
        int idCliente = jsonNode.get("idCliente").asInt();
        int idVendedor = jsonNode.get("idVendedor").asInt();
        int idCarrinho = jsonNode.get("idCarrinho").asInt();
        boolean pedidoAberto = jsonNode.get("pedidoAberto").asBoolean();

        LocalDate dataPedido = LocalDate.parse(jsonNode.get("dataPedido").asText());

        if (validarCarrinho(idCarrinho, idCliente) == false) {
            return 4;
        }

        Pedido pedido = new Pedido();
        pedido.setPrecoTotal(precoTotal);
        pedido.setIdCliente(idCliente);
        pedido.setIdVendedor(idVendedor);
        pedido.setDataPedido(dataPedido);
        pedido.setIdCarrinho(idCarrinho);
        pedido.setPedidoAberto(pedidoAberto);

        List<ItemPedido> itensPedido = new ArrayList<>();
        JsonNode itensPedidoNode = jsonNode.get("itensPedido");
        if (itensPedidoNode != null && itensPedidoNode.isArray()) {
            for (JsonNode itemJson : itensPedidoNode) {
                JsonNode idProdutoNode = itemJson.get("idProduto");
                JsonNode quantidadeNode = itemJson.get("quantidade");

                if (idProdutoNode != null && quantidadeNode != null) {
                    int idProduto = idProdutoNode.asInt();
                    int quantidade = quantidadeNode.asInt();

                    CompraBuscarProdutoDto p = produtoService.produtoPeloId(idProduto);
                    if(p == null){
                        return 1;
                    }

                    String produtoJson = objectMapper.writeValueAsString(p);

                    int validarEstoque = produtoService.verificarEstoqueDisponível(produtoJson, quantidade);

                    if (validarEstoque == 1) {
                        return 1;
                    } else if (validarEstoque == 3) {
                        return 2;
                    }

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setPedido(pedido);
                    itemPedido.setIdProduto(idProduto);
                    itemPedido.setQuantidade(quantidade);

                    itensPedido.add(itemPedido);
                }
            }
        }

        pedido.setItensPedido(itensPedido);

        pdao.save(pedido);

        List<Long> idsProduto = new ArrayList<>();
        for (ItemPedido itemPedido : pedido.getItensPedido()) {
            idsProduto.add(itemPedido.getIdProduto());
        }

        NotaVenda notaVenda = new NotaVenda();
        notaVenda.setPedido(pedido);
        notaVenda.setIdCliente(idCliente);
        notaVenda.setIdVendedor(idVendedor);
        notaVenda.setValorTotal(precoTotal);
        notaVenda.setDataEmissao(LocalDate.now());

        notaVendaDao.save(notaVenda);

        return 3;
    }

    public String calcularFretPedido(FretPedido fretPedido) {
        String numeroString = fretPedido.getCep();

        if(numeroString.length() != 8){
            throw new EntityNotFoundException("Formato de cep inválido!");
        }

        String fret = fretService.calcularFret(fretPedido.getCep(), fretPedido.getQtdeVolume());
        return fret;
    }

}
