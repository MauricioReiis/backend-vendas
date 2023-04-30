package br.com.backEndVendas.service.dao;

import br.com.backEndVendas.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemDao  extends JpaRepository<Item, Integer> {

    Optional<Item> findById(int i);

}
