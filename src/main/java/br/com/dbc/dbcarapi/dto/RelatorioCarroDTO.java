package br.com.dbc.dbcarapi.dto;

import br.com.dbc.dbcarapi.enums.StatusCarro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioCarroDTO {

    private Integer idCarro;
    private StatusCarro status;
    private String modelo;
    private String marca;
    private Double precoDiaria;
    private LocalDate dataAluguel;
    private LocalDate dataEntrega;
    private String nomeCliente;
    private String cpfCliente;
    private String emailCliente;
}
