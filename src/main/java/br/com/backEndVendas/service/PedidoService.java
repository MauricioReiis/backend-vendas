package br.com.backEndVendas.service;

import br.com.backEndVendas.model.*;
import br.com.backEndVendas.service.dao.ItemPedidoDao;
import br.com.backEndVendas.service.dao.NotaVendaDao;
import br.com.backEndVendas.service.dao.PedidoDao;
import br.com.backEndVendas.service.dto.CompraBuscarProdutoDto;
import br.com.backEndVendas.service.dto.CompraCarrinhoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public String realizarPedido(Pedido pedidoJson) throws Exception {
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


    public int processarPedido(Pedido pedidoJson) throws JsonProcessingException {
        if (!validarCarrinho(pedidoJson.getIdCarrinho(), pedidoJson.getIdCliente())) {
            return 4;
        }

        Pedido pedido = new Pedido();
        pedido.setPrecoTotal(pedidoJson.getPrecoTotal());
        pedido.setIdCliente(pedidoJson.getIdCliente());
        pedido.setIdVendedor(pedidoJson.getIdVendedor());
        pedido.setDataPedido(pedidoJson.getDataPedido());
        pedido.setIdCarrinho(pedidoJson.getIdCarrinho());
        pedido.setStatusPedido(pedidoJson.getStatusPedido());

        List<ItemPedido> itensPedido = new ArrayList<>();
        for (ItemPedido itemJson : pedidoJson.getItensPedido()) {
            CompraBuscarProdutoDto p = produtoService.produtoPeloId(itemJson.getIdProduto());
            if (p == null) {
                return 1;
            }

            int validarEstoque = produtoService.verificarEstoqueDisponível(itemJson.getIdProduto(), itemJson.getQuantidade());

            if (validarEstoque == 1) {
                return 1;
            } else if (validarEstoque == 3) {
                return 2;
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setIdProduto(itemJson.getIdProduto());
            itemPedido.setQuantidade(itemJson.getQuantidade());
            itensPedido.add(itemPedido);
        }

        pedido.setItensPedido(itensPedido);

        pdao.save(pedido);

        List<Integer> idsProduto = new ArrayList<>();
        for (ItemPedido itemPedido : pedido.getItensPedido()) {
            idsProduto.add(itemPedido.getIdProduto());
        }

        NotaVenda notaVenda = new NotaVenda();
        notaVenda.setPedido(pedido);
        notaVenda.setIdCliente(pedidoJson.getIdCliente());
        notaVenda.setIdVendedor(pedidoJson.getIdVendedor());
        notaVenda.setValorTotal(pedidoJson.getPrecoTotal());
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

    public String  valorMensalVendedor(int idVendedor, int ano, int mes){

        double somaValor = 0;

        List<Pedido> listaPedidos = pdao.findByIdVendedor(idVendedor);
        for(Pedido p : listaPedidos){
            if(p.getDataPedido().getYear()==ano){
                if(p.getDataPedido().getMonthValue()==mes){
                    somaValor += p.getPrecoTotal();
                }
            }
        }
        return somaValor > 0 ? "{valorVenda: "+somaValor+"}": "Não existe venda!";
    }

}
