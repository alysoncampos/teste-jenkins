package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateDTO {

    @Schema(description = "Nome do cliente")
    @NotBlank(message = "Nome em branco")
    private String nome;

    @Schema(description = "Data de nascimento")
    @Past(message = "insira uma data válida no passado e no formato aaaa-mm-dd")
    private LocalDate dataNascimento;

    @Schema(description = "CPF do cliente")
    @NotBlank(message = "cpf em branco")
    @CPF(message = "Informe um cpf válido")
    private String cpf;

    @Schema(description = "Telefone para contato")
    @NotBlank(message = "telefone em branco")
    private String telefone;

    @Schema(description = "E-mail do cliente")
    @NotBlank(message = "email em branco")
    @Email(message = "informe um email válido")
    private String email;

    @Schema(description = "Usuário do cliente")
    private UsuarioLoginDTO usuarioLogin;
}
