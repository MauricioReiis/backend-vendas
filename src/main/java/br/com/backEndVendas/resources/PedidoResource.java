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
            boolean pedidoProcessado = pServ.processarPedido(pedidoJson);
            if (pedidoProcessado) {
                String mensagemDeRetorno = "Pedido efetuado com sucesso!";
                return ResponseEntity.ok(mensagemDeRetorno);
            } else {
                String mensagemDeRetorno = "Quantidade indisponível ou produto não existente. Pedido não processado.";
                return ResponseEntity.badRequest().body(mensagemDeRetorno);
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("buscar/{id}")
    public Pedido getPedido(@PathVariable int id){
        return pServ.buscarPedidoPeloId(id);
    }

}
