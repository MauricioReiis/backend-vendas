package br.com.backEndVendas.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Pedido")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itensPedido = new ArrayList<>();
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private NotaVenda notaVenda;

    @NotNull(message = "O preço total não pode ser nulo!")
    private double precoTotal;
    private int idCliente;
    private int idVendedor;

    private int idCarrinho;

    @NotNull(message = "O status do pedido não pode estar nulo!")
    @NotBlank(message = "O status do pedido não pode estar vazio!")
    private String statusPedido;
    private LocalDate dataPedido;


}