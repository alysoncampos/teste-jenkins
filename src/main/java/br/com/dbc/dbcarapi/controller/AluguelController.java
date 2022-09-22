package br.com.dbc.dbcarapi.controller;

import br.com.dbc.dbcarapi.config.Response;
import br.com.dbc.dbcarapi.dto.*;
import br.com.dbc.dbcarapi.dto.AluguelCreateDTO;
import br.com.dbc.dbcarapi.dto.AluguelDTO;
import br.com.dbc.dbcarapi.dto.AluguelUpdateDTO;
import br.com.dbc.dbcarapi.dto.RelatorioAluguelDTO;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.service.AluguelService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/aluguel")
@Validated
@RequiredArgsConstructor
public class AluguelController {

    private final AluguelService aluguelService;

    @Operation(summary = "Listar aluguéis paginados", description = "Realizará a listagem de todos os aluguéis no banco de dados")
    @Response
    @GetMapping
    public ResponseEntity<PageDTO<AluguelDTO>> list(Integer pagina, Integer registro) throws RegraDeNegocioException {
        return new ResponseEntity<>(aluguelService.list(pagina, registro), HttpStatus.OK);
    }

    @Operation(summary = "Listar relatórios de aluguéis", description = "Realizará a listagem dos relatórios dos aluguéis do banco de dados")
    @Response
    @GetMapping("/relatorio-aluguel")
    public ResponseEntity<List<RelatorioAluguelDTO>> listRelatorioAluguel(@RequestParam(required = false) Integer idAluguel) throws RegraDeNegocioException {
        return new ResponseEntity<>(aluguelService.listRelatorioAluguel(idAluguel), HttpStatus.OK);
    }

    @GetMapping("/meus-alugueis")
    public ResponseEntity<List<AluguelDTO>> listAluguelByIdCliente() throws RegraDeNegocioException {
        return new ResponseEntity<>(aluguelService.listAluguelByIdCliente(), HttpStatus.OK);
    }

    @Operation(summary = "Realizar aluguel de um carro", description = "Realizará um aluguel de um veículo da DBCar.")
    @Response
    @PostMapping("/{idCarro}")
    public ResponseEntity<AluguelDTO> create(@PathVariable("idCarro") Integer idCarro,
                                             @RequestBody @Valid AluguelCreateDTO aluguel) throws RegraDeNegocioException {
        return new ResponseEntity<>(aluguelService.create(idCarro, aluguel), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar um aluguel", description = "Atualizará um aluguel de um cliente")
    @Response
    @PutMapping("/{idAluguel}")
    public ResponseEntity<AluguelDTO> update(@PathVariable("idAluguel") Integer idAluguel,
                                             @RequestBody @Valid AluguelUpdateDTO aluguelUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(aluguelService.update(idAluguel, aluguelUpdateDTO), HttpStatus.OK);
    }

    @Operation(summary = "Finalizar aluguel e devolver veiculo", description = "Realizará a finalização do aluguel e devolução do carro")
    @Response
    @PutMapping("/devolver-carro/{idAluguel}")
    public ResponseEntity<AluguelDTO> devolverCarro(@PathVariable("idAluguel") Integer idAluguel) throws RegraDeNegocioException {
        return new ResponseEntity<>(aluguelService.devolverCarro(idAluguel), HttpStatus.OK);
    }

    @Operation(summary = "Cancelar aluguel", description = "Cancelar um aluguel associado ao ID do banco de dados")
    @Response
    @PutMapping("/cancelar-contrato/{idAluguel}")
    public ResponseEntity<Void> cancelarAluguel(@PathVariable("idAluguel") Integer idAluguel) throws RegraDeNegocioException {
        aluguelService.cancelarAluguel(idAluguel);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
