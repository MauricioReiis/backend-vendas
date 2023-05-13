package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "devolucao")
public class DevolucaoPedido {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id; // código do id da tabela
        private int codigoPedido; // código do pedido devolvido

        private String dataDevolucao; // data da devolução

        private String motivoDevolucao; // motivo da devolução


}
