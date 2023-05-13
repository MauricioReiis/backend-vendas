package br.com.backEndVendas;

import br.com.backEndVendas.model.Produto;
import br.com.backEndVendas.service.dao.ProdutoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackEndVendasApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackEndVendasApplication.class, args);
	}

	@Autowired
	ProdutoDao produtoDao;

	@Override
	public void  run(String... args) throws  Exception{
		Produto a = new Produto();
		a.setNomeProduto("pneu");
		a.setQtdEstoque(20);
		a.setPrecoUnit(20);

		Produto b = new Produto();
		b.setNomeProduto("porta");
		b.setQtdEstoque(20);
		b.setPrecoUnit(100);

		Produto c = new Produto();
		c.setNomeProduto("madeira");
		c.setQtdEstoque(20);
		c.setPrecoUnit(200);

		produtoDao.save(a);
		produtoDao.save(b);
		produtoDao.save(c);

	}

}
