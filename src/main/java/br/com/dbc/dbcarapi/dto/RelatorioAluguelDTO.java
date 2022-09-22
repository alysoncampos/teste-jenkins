package br.com.dbc.dbcarapi.dto;

import br.com.dbc.dbcarapi.enums.ClasseCarro;
import br.com.dbc.dbcarapi.enums.StatusCarro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAluguelDTO {

    private Integer idAluguel;
    private LocalDate dataAluguel;
    private LocalDate dataEntrega;
    private String modelo;
    private String marca;
    private ClasseCarro classe;
    private String placa;
    private Double precoDiaria;
    private Double valorTotal;
    private StatusCarro status;
    private String nomeFuncionario;
    private String matriculaFuncionario;
    private String nomeCliente;
    private String cpfCliente;
    private String telefoneCliente;
    private String emailCliente;
}
