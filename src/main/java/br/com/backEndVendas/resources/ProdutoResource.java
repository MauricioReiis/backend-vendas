package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/produtos")
@RestController
public class ProdutoResource {

    @Autowired
    ProdutoService prodServ;

    @PostMapping
    public Produto adicionarProduto(@RequestBody Produto produto){
        return prodServ.save(produto);
    }
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> updadeProduto(@PathVariable int id, @RequestBody Produto produto){
        try{
            return ResponseEntity.ok(prodServ.updateProduto(id, produto));
        }
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("cancelar/{id}")
    public ResponseEntity<String> cancelarProduto(@PathVariable int id){
        try {
            return  ResponseEntity.ok(prodServ.cancelarProduto(id));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("buscar/{id}")
    public Produto getProduto(@PathVariable int id){
        return prodServ.buscarProdutoPeloId(id);
    }

}
