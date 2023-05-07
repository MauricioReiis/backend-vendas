package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "NotaFiscal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int idPedido;
    private double valorTotal;
    private int idCliente;
    private LocalDate dataPedido;

    public NotaFiscal(Pedido pedido) {
        idPedido = pedido.getIdPedido();

        idCliente = pedido.getIdCliente();
        dataPedido = pedido.getDataPedido();
    }
}
