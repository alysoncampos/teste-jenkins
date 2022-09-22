package br.com.dbc.dbcarapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecuperarSenhaDTO {

    @NotBlank
    private String senha;
}
