package br.com.backEndVendas.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "quantidade")
    private int quantidade;


    //@ManyToOne
    //@JoinColumn(name = "id_item")
    //private Produto produto;

    @Column(name = "preco_unitario")
    @NotNull(message = "Esse campo não pode ser nulo")
    @NotBlank(message = "Esse campo não pode estar vazio")
    private String precoUnitario;


}
