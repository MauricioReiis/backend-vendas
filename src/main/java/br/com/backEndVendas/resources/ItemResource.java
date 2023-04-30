package br.com.backEndVendas.resources;


import br.com.backEndVendas.model.Item;
import br.com.backEndVendas.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemResource {

    @Autowired
    ItemService iServ;

    @PostMapping
    public Item saveItem(@RequestBody Item item) {
        if (iServ.isValid(item)) {
            return iServ.save(item);
        } else {
            return null;
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarItem(@PathVariable int id, @Valid @RequestBody Item item) {
        try {
            return ResponseEntity.ok(iServ.atualizarItem(id, item));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/listar/{id}")
    public Item getItemById(@PathVariable int id) {
        return iServ.buscaItemPeloId(id);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity apagaItem(@PathVariable int id) {
        try {
            return ResponseEntity.ok(iServ.apagarItem(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/listar/todos")
    public ResponseEntity<List<Item>> listaTodosItens() {
        return ResponseEntity.ok(iServ.exibeTodosItens());
    }

}
