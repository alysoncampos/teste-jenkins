package br.com.dbc.dbcarapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AluguelCreateDTO {

    @Schema(description = "Funcionario que alugou o carro.")
    @NotNull
    private Integer idFuncionario;

    @Schema(description = "Data que o carro foi alugado")
    @Past
    @NotNull
    private LocalDate dataAluguel;

    @Schema(description = "Data que o carro foi devolvido")
    @NotNull
    private LocalDate dataEntrega;
}
