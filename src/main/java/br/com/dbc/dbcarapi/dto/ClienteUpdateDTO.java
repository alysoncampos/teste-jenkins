package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteUpdateDTO {

    @Schema(description = "Nome do cliente")
    private String nome;

    @Schema(description = "Data de nascimento")
    private LocalDate dataNascimento;

    @Schema(description = "CPF do cliente")
    private String cpf;

    @Schema(description = "Telefone para contato")
    private String telefone;

    @Schema(description = "E-mail do cliente")
    private String email;
}
