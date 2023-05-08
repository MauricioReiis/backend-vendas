package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pedido")
@RestController
public class PedidoResource {

    @Autowired
    PedidoService pServ;

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
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> updadePedido(@PathVariable int id, @RequestBody Pedido pedido){
        try{
            return ResponseEntity.ok(pServ.updatePedido(id, pedido));
        }
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("cancelar/{id}")
    public ResponseEntity<String> cancelarPedido(@PathVariable int id){
        try {
            return  ResponseEntity.ok(pServ.cancelarPedido(id));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("buscar/{id}")
    public Pedido getPedido(@PathVariable int id){
        return pServ.buscarPedidoPeloId(id);
    }

}
