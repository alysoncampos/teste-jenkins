package br.com.dbc.dbcarapi.dto;

import br.com.dbc.dbcarapi.config.PlacaCarro;
import br.com.dbc.dbcarapi.enums.ClasseCarro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroCreateDTO {

    @Schema(description = "Modelo do carro")
    @NotBlank(message = "O modelo do veículo não pode ser nulo/vazio.")
    private String modelo;

    @Schema(description = "Marca do carro")
    @NotBlank(message = "A marca do veículo não pode ser nula/vazia.")
    private String marca;

    @Schema(description = "Classe do carro")
    @NotNull(message = "A classe do veículo não pode ser nula/vazia.")
    private ClasseCarro classe;

    @Schema(description = "Placa do carro")
    @NotBlank(message = "A placa do veículo não pode ser nula/vazia.")
    @PlacaCarro
    private String placa;

    @Schema(description = "Preço da diária do veículo")
    @NotNull(message = "O veículo não pode ter um preço de diária nulo.")
    private Double precoDiaria;
}
