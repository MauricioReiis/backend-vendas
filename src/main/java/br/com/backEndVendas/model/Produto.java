package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    private int idProduto;
    private String nomeProduto;
    private  int qtdEstoque;
    private double precoUnit;

}
