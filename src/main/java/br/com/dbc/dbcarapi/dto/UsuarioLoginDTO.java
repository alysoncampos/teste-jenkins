package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLoginDTO {

    @NotBlank
    @Schema(example = "admin")
    private String login;

    @NotBlank
    @Schema(example = "123")
    private String senha;
}
