package br.com.backEndVendas.service;

import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.dao.PedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidoService {

    @Autowired
    PedidoDao pdao;

    public Pedido save(Pedido pedido){
        return pdao.save(pedido);
    }
}
