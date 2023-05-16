package br.com.backEndVendas.service;

import br.com.backEndVendas.model.DevolucaoPedido;
import br.com.backEndVendas.service.dao.DevolucaoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class DevolucaoPedidoService {

    @Autowired
    private DevolucaoDao devolucaoDAO;

    public DevolucaoPedido save(DevolucaoPedido devolucaoPedido){

        return devolucaoDAO.save(devolucaoPedido);
    }

}
