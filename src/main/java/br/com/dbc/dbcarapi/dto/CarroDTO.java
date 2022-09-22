package br.com.dbc.dbcarapi.dto;

import br.com.dbc.dbcarapi.enums.ClasseCarro;
import br.com.dbc.dbcarapi.enums.StatusCarro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroDTO {

    @Schema(description = "Identificador (ID) do carro")
    private Integer idCarro;

    @Schema(description = "Status do carro")
    private StatusCarro status;

    @Schema(description = "Modelo do carro")
    private String modelo;

    @Schema(description = "Marca do carro")
    private String marca;

    @Schema(description = "Classe do carro")
    private ClasseCarro classe;

    @Schema(description = "Placa do carro")
    private String placa;

    @Schema(description = "Preço da diária do veículo")
    private Double precoDiaria;
}
