package br.com.backEndVendas.service.dto;

import br.com.backEndVendas.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDto {
     private Pedido pedido;

}
