package br.com.backEndVendas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FretPedido {

    @NotNull(message = "O cep não pode ser nulo!")
    @NotBlank(message = "O cep não pode estar vazio!")
    String cep;

    @NotNull(message = "A quantidade de volume não pode ser nula!")
    int qtdeVolume;

    int idCliente;

}
