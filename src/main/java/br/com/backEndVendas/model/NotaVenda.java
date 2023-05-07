package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NotaVenda")
public class NotaVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idNotaVenda;
    private int idPedido;
    private int idCliente;
    private int idVendedor;
    private double valorTotal;
    private LocalDate dataEmissao;


    public NotaVenda(Pedido pedido) {
        idPedido = pedido.getIdPedido();
        valorTotal = pedido.getPrecoTotal();
        dataEmissao= pedido.getDataPedido();
    }
}
