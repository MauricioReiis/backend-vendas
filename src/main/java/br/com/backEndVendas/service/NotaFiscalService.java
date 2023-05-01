package br.com.backEndVendas.service;

import br.com.backEndVendas.model.NotaFiscal;
import br.com.backEndVendas.service.dao.NotaFiscalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class NotaFiscalService {

    @Autowired
    NotaFiscalDao nfdao;

    public NotaFiscal save(NotaFiscal notaFiscal){return nfdao.save(notaFiscal);
    }

    public NotaFiscal buscarPedidoPeloId(int id) {
        Optional<NotaFiscal> notaFiscalOptional = nfdao.findById(id);
        if (notaFiscalOptional.isPresent()) {
            return notaFiscalOptional.get();
        } else {
            throw new EntityNotFoundException("Nota fiscal não encontrada");
        }
    }
}
