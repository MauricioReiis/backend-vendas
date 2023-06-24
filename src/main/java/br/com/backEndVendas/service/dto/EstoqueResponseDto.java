package br.com.backEndVendas.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueResponseDto {
    private boolean status;
    private int quantidade;
}