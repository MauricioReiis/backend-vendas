package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.NotaVenda;
import br.com.backEndVendas.service.NotaVendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pedido/nota")
@RestController
public class NotaVendaResource {
    @Autowired
    NotaVendaService nfServ;

    @GetMapping("/{id}")
    public NotaVenda getNotaFiscal(@PathVariable int id) {
        return nfServ.buscarNotaPeloId(id);
    }

}