package br.com.backEndVendas.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item")
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
    private double precoUnitario;


}
