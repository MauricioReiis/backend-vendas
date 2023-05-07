package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ItemPedido")
public class ItemPedido{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idItemPedido;
    @ManyToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
    private int idProduto;
    private int quantidade;

    public ItemPedido(Pedido pedido) {
        System.out.println(pedido);
        idProduto = pedido.getIdCliente();
        quantidade = pedido.getIdVendedor();

    }

}
