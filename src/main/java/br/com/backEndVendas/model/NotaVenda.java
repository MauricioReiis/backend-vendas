package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "NotaVenda")
public class NotaVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;

    private long idCliente;

    private long idVendedor;

    private double valorTotal;

    private LocalDate dataEmissao;

    @ElementCollection
    private List<Long> idsProduto;

}