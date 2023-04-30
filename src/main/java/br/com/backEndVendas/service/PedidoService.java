package br.com.backEndVendas.service;

import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.dao.PedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PedidoService {

    @Autowired
    PedidoDao pdao;

    public Pedido save(Pedido pedido){
        return pdao.save(pedido);
    }

    public Pedido updatePedido(int id, Pedido pedido) throws Exception {
        Pedido p = buscarPedidoPeloId(id);
        if (p == null){
            throw new Exception("Pedido não encontrado");
        }
        p.setIdItem(pedido.getIdItem());
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
}
