package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.NotaVenda;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pedido")
@RestController
public class PedidoResource {

    @Autowired
    PedidoService pServ;

   // @PostMapping
 //   public NotaVenda adicionarPedido(@RequestBody Pedido pedido){
   //     return pServ.save(pedido);
    //}

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody String pedidoJson) {
        try {
            pServ.processarPedido(pedidoJson);
            return ResponseEntity.ok().build();
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
