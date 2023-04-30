package br.com.backEndVendas.service;

import br.com.backEndVendas.model.Item;
import br.com.backEndVendas.service.dao.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.Double.parseDouble;

@Service
public class ItemService {

    @Autowired
    ItemDao iDao;

    public boolean isValid(Item item){
        return (parseDouble(item.getPrecoUnitario()) > 0);
    }

    public Item save(Item item){
        return iDao.save(item);
    }

    public Item buscaItemPeloId(int id){
        Optional<Item> oi = iDao.findById(id);

        if(oi.isPresent()){
            return oi.get();
        } else {
            return null;
        }
    }

    public String apagarItem(int id) throws Exception {
        Item i = buscaItemPeloId(id);

        if (i == null) {
            throw new Exception("Item não encontrado");
        } else {
            iDao.delete(i);
            return "Item " + id + " apagado com sucesso!";
        }
    }

    public Item atualizarItem(int id, Item item) throws Exception {
        Item i = buscaItemPeloId(id);
        if (i == null) {
            throw new Exception("Item não encontrado!");
        }

        i.setQuantidade(item.getQuantidade());
        i.setPrecoUnitario(item.getPrecoUnitario());

        return iDao.save(i);
    }


    public List<Item> exibeTodosItens() {
        return iDao.findAll();
    }


}
