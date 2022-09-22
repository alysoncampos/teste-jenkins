package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @Schema(description = "Id do Usuário")
    private Integer idUsuario;

    @Schema(description = "Login do Usuário")
    private String login;
}
