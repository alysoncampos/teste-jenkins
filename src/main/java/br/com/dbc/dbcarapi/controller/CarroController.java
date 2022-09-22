package br.com.dbc.dbcarapi.controller;

import br.com.dbc.dbcarapi.config.Response;
import br.com.dbc.dbcarapi.dto.*;
import br.com.dbc.dbcarapi.dto.CarroCreateDTO;
import br.com.dbc.dbcarapi.dto.CarroDTO;
import br.com.dbc.dbcarapi.dto.RelatorioCarroDTO;
import br.com.dbc.dbcarapi.enums.ClasseCarro;
import br.com.dbc.dbcarapi.enums.StatusCarro;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.service.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/carro")
@Validated
@RequiredArgsConstructor
public class CarroController {

    private final CarroService carroService;

    @Operation(summary = "Listagem de carros com paginação e filtro", description = "Realizará a listagem paginada das informações do carro.")
    @Response
    @GetMapping
    public ResponseEntity<PageDTO<CarroDTO>> listInfoCarro(@RequestParam(required = false) Integer idCarro,
                                                           @RequestParam(required = false) ClasseCarro classe,
                                                           @RequestParam(required = false) StatusCarro status,
                                                           Integer pagina, Integer registro ) {
        return new ResponseEntity<>(carroService.listInfoCarro(idCarro, classe, status, pagina, registro), HttpStatus.OK);
    }

    @Operation(summary = "Listar relatórios dos carros", description = "Realizará a listagem dos relatórios dos carros do banco de dados")
    @Response
    @GetMapping("/relatorio-carro")
    public ResponseEntity<List<RelatorioCarroDTO>> listRelatorioCarro(@RequestParam(required = false) Integer idCarro) throws RegraDeNegocioException {
        return new ResponseEntity<>(carroService.listRelatorioCarro(idCarro), HttpStatus.OK);
    }

    @Operation(summary = "Adicionar carro ao catálogo", description = "Adicionará um novo carro ao catálogo")
    @Response
    @PostMapping
    public ResponseEntity<CarroDTO> create(@RequestBody @Valid CarroCreateDTO carro) throws RegraDeNegocioException {
        return new ResponseEntity<>(carroService.create(carro), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar carro do catálogo", description = "Atualizará um carro do catálogo")
    @Response
    @PutMapping("/{idCarro}")
    public ResponseEntity<CarroDTO> update(@PathVariable("idCarro") Integer idCarro,
                                           @RequestBody @Valid CarroCreateDTO carroAtualizar) throws RegraDeNegocioException {
        return new ResponseEntity<>(carroService.update(idCarro, carroAtualizar), HttpStatus.OK);
    }

    @Operation(summary = "Deletar carro do catálogo", description = "Deletará o carro e todos os seus dados do banco de dados")
    @Response
    @DeleteMapping("/{idCarro}")
    public ResponseEntity<Void> delete(@PathVariable("idCarro") Integer idCarro) throws RegraDeNegocioException {
        carroService.delete(idCarro);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}