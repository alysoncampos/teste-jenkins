package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDTO {

    @Schema(description = "Comissão por aluguel.")
    private Double comissao;

    @Schema(description = "Identificador (ID) do funcionário")
    private Integer idFuncionario;
    
    @Schema(description = "Nome do funcionário.")
    private String nome;
    
    @Schema(description = "Matricula do funcionário.")
    private String matricula;
    
    @Schema(description = "Salário do funcionário.")
    private Double salario;
    
    @Schema(description = "Dados do funcionario")
    private UsuarioDTO usuario;
}
