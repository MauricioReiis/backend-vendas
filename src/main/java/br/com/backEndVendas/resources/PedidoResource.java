package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.PedidoService;
import br.com.backEndVendas.service.dao.PedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/pedido")
@RestController
public class PedidoResource {

    @Autowired
    PedidoService pServ;

    @Autowired
    PedidoDao pdao;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody String pedidoJson) {
        try {
            pServ.processarPedido(pedidoJson);
            String mensagemDeRetorno = "Pedido efetuado com sucesso!";
            return ResponseEntity.ok(mensagemDeRetorno);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("buscar/{id}")
    public Pedido getPedido(@PathVariable int id){
        return pServ.buscarPedidoPeloId(id);
    }

}
