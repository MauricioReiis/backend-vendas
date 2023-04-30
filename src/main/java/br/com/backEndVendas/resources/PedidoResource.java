package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pedido")
@RestController
public class PedidoResource {

    @Autowired
    PedidoService pServ;

    @PostMapping
    public Pedido adicionarPedido(@RequestBody Pedido pedido){
        return pServ.save(pedido);
    }
}
