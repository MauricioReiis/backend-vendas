package br.com.backEndVendas.service;

import br.com.backEndVendas.model.ItemPedido;
import br.com.backEndVendas.model.NotaVenda;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.dao.ItemPedidoDao;
import br.com.backEndVendas.service.dao.NotaVendaDao;
import br.com.backEndVendas.service.dao.PedidoDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PedidoService {

    @Autowired
    PedidoDao pdao;
    @Autowired
    NotaVendaDao notaVendaDao;

    @Autowired
    ItemPedidoDao itemPedidoDao;


    public Pedido updatePedido(int id, Pedido pedido) throws Exception {
        Pedido p = buscarPedidoPeloId(id);
        if (p == null){
            throw new Exception("Pedido não encontrado");
        }

        p.setPrecoTotal(pedido.getPrecoTotal());
        p.setIdCliente(pedido.getIdCliente());
        p.setDataPedido(pedido.getDataPedido());

        return  pdao.save(p);

    }

    public  Pedido buscarPedidoPeloId(int id){
        Optional<Pedido> op = pdao.findById(id);
        if (op.isPresent()){
            return  op.get();
        }else {
            return null;
        }
    }

    public String cancelarPedido(int id) throws Exception {
        Pedido p = buscarPedidoPeloId(id);
        if (p == null){
            throw new Exception("Pedido não encontrado");
        }
        pdao.delete(p);
        return "Pedido cancelado com sucesso!";
    }


    public void processarPedido(String pedidoJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(pedidoJson);

        double precoTotal = jsonNode.get("precoTotal").asDouble();
        int idCliente = jsonNode.get("idCliente").asInt();
        int idVendedor = jsonNode.get("idVendedor").asInt();
        LocalDate dataPedido = LocalDate.parse(jsonNode.get("dataPedido").asText());

        Pedido pedido = new Pedido();
        pedido.setPrecoTotal(precoTotal);
        pedido.setIdCliente(idCliente);
        pedido.setIdVendedor(idVendedor);
        pedido.setDataPedido(dataPedido);

        List<ItemPedido> itensPedido = new ArrayList<>();
        for (JsonNode itemJson : jsonNode.get("itensPedido")) {
            int idProduto = itemJson.get("idProduto").asInt();
            int quantidade = itemJson.get("quantidade").asInt();

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setIdProduto(idProduto);
            itemPedido.setQuantidade(quantidade);

            itensPedido.add(itemPedido);
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
    }
}
