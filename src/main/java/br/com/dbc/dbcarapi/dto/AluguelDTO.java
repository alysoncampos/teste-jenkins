package br.com.dbc.dbcarapi.dto;

import br.com.dbc.dbcarapi.enums.StatusAluguel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AluguelDTO {

    @Schema(description = "Identificador (ID) do aluguel")
    private Integer idAluguel;

    @Schema(description = "Descritivo do status do aluguel")
    private StatusAluguel status;

    @Schema(description = "Valor total do aluguel")
    private Double valorTotal;

    @Schema(description = "Data que o carro foi alugado")
    private LocalDate dataAluguel;

    @Schema(description = "Data que o carro foi devolvido")
    private LocalDate dataEntrega;

    @Schema(description = "Cliente associado ao aluguel.")
    private ClienteDTO cliente;

    @Schema(description = "Carro alugado")
    private CarroDTO carro;

    @Schema(description = "Funcionário associado ao aluguel.")
    private FuncionarioDTO funcionario;
}
