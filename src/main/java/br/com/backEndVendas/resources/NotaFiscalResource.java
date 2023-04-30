package br.com.backEndVendas.resources;

import br.com.backEndVendas.model.NotaFiscal;
import br.com.backEndVendas.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notafiscal")
@RestController
public class NotaFiscalResource {

    @Autowired
    NotaFiscalService nfServ;

    @PostMapping
    public NotaFiscal adicionarPedido(@RequestBody NotaFiscal notaFiscal){
        return nfServ.save(notaFiscal);
    }

}
