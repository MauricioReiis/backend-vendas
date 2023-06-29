package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "devolucao")
public class DevolucaoPedido {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        private int codigoPedido, codigoProduto, qtdeDevolvida;

        private LocalDate dataDevolucao;

        public DevolucaoPedido(int codigoPedido, int codigoProduto, int qtdeDevolvida, LocalDate dataDevolucao) {
                this.codigoPedido = codigoPedido;
                this.codigoProduto = codigoProduto;
                this.qtdeDevolvida = qtdeDevolvida;
                this.dataDevolucao = dataDevolucao;
        }
}
