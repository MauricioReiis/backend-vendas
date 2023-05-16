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
        private int id;
        private int codigoPedido;

        private String dataDevolucao;

        private String motivoDevolucao;


}
