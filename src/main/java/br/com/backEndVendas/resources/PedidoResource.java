package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.FretPedido;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.PedidoService;
import br.com.backEndVendas.service.dao.PedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/pedido")
@RestController
public class PedidoResource {

    @Autowired
    PedidoService pServ;

    @Autowired
    PedidoDao pdao;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody String pedidoJson) {

        try{
            return ResponseEntity.ok(pServ.formatarResposta(pServ.realizarPedido(pedidoJson)));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("buscar/{id}")
    public Pedido getPedido(@PathVariable int id){
        return pServ.buscarPedidoPeloId(id);
    }
    @PostMapping("/calcular/fret")
    public ResponseEntity<?> calcularFretPedido(@RequestBody FretPedido fretPedido){
        try{
            return ResponseEntity.ok(pServ.calcularFretPedido(fretPedido));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
