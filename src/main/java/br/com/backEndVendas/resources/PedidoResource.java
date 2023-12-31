package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.DevolucaoPedido;
import br.com.backEndVendas.model.FretPedido;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.PedidoService;
import br.com.backEndVendas.service.dao.PedidoDao;
import br.com.backEndVendas.service.dto.PagamentosCarrinhoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/pedido")
@RestController
public class PedidoResource {

    @Autowired
    PedidoService pServ;

    @Autowired
    PedidoDao pdao;

    @GetMapping
    public String home() {
        return "Serviços ON";
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody Pedido pedidoJson) {
        try{
            return ResponseEntity.ok(pServ.realizarPedido(pedidoJson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<?> getPedido(@PathVariable int id) {
        {
            try{
                return ResponseEntity.ok(pServ.buscarPedidoPeloId(id));
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }

        }
    }

    @PostMapping("/calcular/fret")
    public ResponseEntity<?> calcularFretPedido(@Valid @RequestBody FretPedido fretPedido){
        try{
            return ResponseEntity.ok(pServ.calcularFretPedido(fretPedido));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/calcular/vendedor/{idVendedor}/{ano}/{mes}")
    public ResponseEntity<?> getValorVendedor(@PathVariable int idVendedor, @PathVariable int ano, @PathVariable int mes) {
        try {
            return ResponseEntity.ok(pServ.valorMensalVendedor(idVendedor, ano, mes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

      }
    @GetMapping("/buscar/vendas/vendedor/{idVendedor}")
    public ResponseEntity<?> getComprasValorVendedor(@PathVariable int idVendedor)
    {
        try{
            return ResponseEntity.ok(pServ.comprasValorVendedor(idVendedor));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @GetMapping("/devolucao/{idPedido}/{idProduto}/{qtdeDevolvida}")
    public ResponseEntity<?> devolverPedido(@PathVariable int idPedido, @PathVariable int idProduto, @PathVariable int qtdeDevolvida) {
        try {
            return ResponseEntity.ok(pServ.atualizarStatus(idPedido, idProduto, qtdeDevolvida));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/calcular/vendedor/anual/{idVendedor}/{ano}")
    public ResponseEntity<?> getValorAnualVendedor(@PathVariable int idVendedor, @PathVariable int ano) {
        try {
            return ResponseEntity.ok(pServ.valorAnualVendedor(idVendedor, ano));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscar/valor/pedido/{idPedido}")
    public ResponseEntity<?> getValorPedido(@PathVariable int idPedido) {
        try {
            return ResponseEntity.ok(pServ.getValorPedido(idPedido));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
