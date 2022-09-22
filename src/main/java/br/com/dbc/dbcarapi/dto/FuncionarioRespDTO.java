package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioRespDTO {
    
    @Schema(description = "Identificador (ID) do funcionário")
    private Integer idFuncionario;
    
    @Schema(description = "Nome do funcionário.")
    private String nome;
}
