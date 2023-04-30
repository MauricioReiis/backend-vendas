package br.com.backEndVendas.service;

import br.com.backEndVendas.model.NotaFiscal;
import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.dao.NotaFiscalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotaFiscalService {

    @Autowired
    NotaFiscalDao nfdao;

    public NotaFiscal save(NotaFiscal notaFiscal){return nfdao.save(notaFiscal);
    }
}
