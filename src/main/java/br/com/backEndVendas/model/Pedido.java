package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idPedido;
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itensPedido;
    private double precoTotal;
    private int idCliente;
    private int idVendedor;
    private LocalDate dataPedido;

}