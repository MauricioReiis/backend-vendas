package br.com.backEndVendas.service;

import br.com.backEndVendas.model.DevolucaoPedido;
import br.com.backEndVendas.model.ItemPedido;
import br.com.backEndVendas.model.Pedido;
import br.com.backEndVendas.service.dao.DevolucaoDao;
import br.com.backEndVendas.service.dao.PedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@RestController
@Service
public class DevolucaoPedidoService {

    @Autowired
    private DevolucaoDao devolucaoDAO;


    RestTemplate rest;

    @Autowired
    PedidoDao pedidoDao;

    public DevolucaoPedido save(DevolucaoPedido devolucaoPedido){

        return devolucaoDAO.save(devolucaoPedido);
    }

}
