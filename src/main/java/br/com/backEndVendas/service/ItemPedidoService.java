package br.com.backEndVendas.service;

import br.com.backEndVendas.model.ItemPedido;
import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.dao.ItemPedidoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ItemPedidoService {


    @Autowired
    ItemPedidoDao ipdao;

    public ItemPedido save(ItemPedido itemPedido){
        System.out.println(itemPedido);
        return ipdao.save(itemPedido);
    }
}
