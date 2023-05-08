package br.com.backEndVendas.service;

import br.com.backEndVendas.model.NotaVenda;
import br.com.backEndVendas.service.dao.NotaVendaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class NotaVendaService {

    @Autowired
    NotaVendaDao nfdao;

    public NotaVenda buscarNotaPeloId(long id) {
        Optional<NotaVenda> notaVendaOptional = nfdao.findById(id);
        if (notaVendaOptional.isPresent()) {
            return notaVendaOptional.get();
        } else {
            throw new EntityNotFoundException("Nota fiscal não encontrada");
        }
    }

}
