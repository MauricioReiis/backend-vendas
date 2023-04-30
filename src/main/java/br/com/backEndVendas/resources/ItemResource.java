package br.com.backEndVendas.resources;


import br.com.backEndVendas.model.Item;
import br.com.backEndVendas.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemResource {

    @Autowired
    ItemService iServ;

    @PostMapping
    public Item saveItem(@RequestBody Item item) {
        if (iServ.isValid(item)) {
            return iServ.save(item);
        } else {
            return null;
        }
    }






}
