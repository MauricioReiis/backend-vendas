package br.com.backEndVendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan(basePackages = "br.com.backEndVendas.model")
@SpringBootApplication
public class BackEndVendasApplication  {

	public static void main(String[] args) {
		SpringApplication.run(BackEndVendasApplication.class, args);
	}

}
