package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.Item;
import br.com.backEndVendas.model.NotaFiscal;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notafiscal")
@RestController
public class NotaFiscalResource {

    @Autowired
    NotaFiscalService nfServ;

    @PostMapping
    public NotaFiscal adicionarNotaFiscal(@RequestBody NotaFiscal notaFiscal) {
        return nfServ.save(notaFiscal);
    }


    @GetMapping("buscar/{id}")
    public NotaFiscal getNotaFiscal(@PathVariable int id) {
        return nfServ.buscarNotaFiscalPeloId(id);
    }


}