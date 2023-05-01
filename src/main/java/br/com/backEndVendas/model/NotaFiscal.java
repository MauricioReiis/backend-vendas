package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "NotaFiscal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotaFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_Nota;
    private int Id_Item;
    private int Id_Pedido;
    private double Valor_Total_Itens;
    private int Quantidade;

//    public NotaFiscal(Pedido pedido, List<Item> itens) {
//        Id_Pedido = pedido.getIdPedido();
//        Quantidade = itens.size();
//        double total = 0.0;
//        for (Item item : itens) {
//            double valorTotal = Double.parseDouble(item.getPrecoUnitario()) * item.getQuantidade();
//        }
//        Valor_Total_Itens = total;
//    }
}

