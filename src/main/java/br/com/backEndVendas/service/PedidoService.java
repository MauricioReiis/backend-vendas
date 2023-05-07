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

    public NotaVenda save(Pedido pedido){
        NotaVenda notaVenda = new NotaVenda(pedido);
        ItemPedido itemPedido = new ItemPedido(pedido);
        itemPedidoDao.save(itemPedido);
        pdao.save(pedido);
        return notaVendaDao.save(notaVenda);
    }

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

    public  Pedido buscarPedidoPeloId(long id){
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

        Double precoTotal = jsonNode.get("precoTotal").asDouble();
        Long idCliente = jsonNode.get("idCliente").asLong();
        Long idVendedor = jsonNode.get("idVendedor").asLong();
        LocalDate dataPedido = LocalDate.parse(jsonNode.get("dataPedido").asText());

        Pedido pedido = new Pedido();
        pedido.setPrecoTotal(precoTotal);
        pedido.setIdCliente(idCliente);
        pedido.setIdVendedor(idVendedor);
        pedido.setDataPedido(dataPedido);

        List<ItemPedido> itensPedido = new ArrayList<>();
        for (JsonNode itemJson : jsonNode.get("itensPedido")) {
            Long idProduto = itemJson.get("idProduto").asLong();
            Integer quantidade = itemJson.get("quantidade").asInt();

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setIdProduto(idProduto);
            itemPedido.setQuantidade(quantidade);

            itensPedido.add(itemPedido);
        }

        pedido.setItensPedido(itensPedido);

        pdao.save(pedido);
    }
}
