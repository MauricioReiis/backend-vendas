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

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itemPedido = new ArrayList<>();

    private double precoTotal;

    private int idCliente;
    private int idVendedor;
    private LocalDate dataPedido;


    public Pedido(List<ItemPedido> itemPedido, double precoTotal, int idCliente, int idVendedor, LocalDate dataPedido) {
        this.itemPedido = new ArrayList<>();
        this.precoTotal = precoTotal;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.dataPedido = dataPedido;
    }
}