package br.com.dbc.dbcarapi.controller;

import br.com.dbc.dbcarapi.config.Response;
import br.com.dbc.dbcarapi.dto.FuncionarioCreateDTO;
import br.com.dbc.dbcarapi.dto.FuncionarioDTO;
import br.com.dbc.dbcarapi.dto.FuncionarioRespDTO;
import br.com.dbc.dbcarapi.dto.FuncionarioUpdateDTO;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/funcionario")
@Validated
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Operation(summary = "Listar os funcionários do banco de dados", description = "Realizará a listagem de todos os funcionários do banco de dados")
    @Response
    @GetMapping("/listar")
    public ResponseEntity<List<FuncionarioDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(funcionarioService.list(), HttpStatus.OK);
    }
    
    @Operation(summary = "Listar os funcionários do banco de dados para aluguel", description = "Realizará a listagem de todos os funcionários do banco de dados para aluguel")
    @Response
    @GetMapping("/listar-para-aluguel")
    public ResponseEntity<List<FuncionarioRespDTO>> listParaAluguel() throws RegraDeNegocioException {
        return new ResponseEntity<>(funcionarioService.listParaAluguel(), HttpStatus.OK);
    }

    @Operation(summary = "Listar os funcionários do banco de dados pela identificação", description = "Realizará a listagem dos funcionários no banco de dados pelo número da identificação")
    @Response
    @GetMapping("/{idFuncionario}")
    public ResponseEntity<FuncionarioDTO> listById(@PathVariable("idFuncionario") Integer idFuncionario) throws RegraDeNegocioException {
        return new ResponseEntity<>(funcionarioService.listByIdFuncionario(idFuncionario), HttpStatus.OK);
    }

    @Operation(summary = "Adicionar um novo funcionário", description = "Adicionará um novo funcionário ao banco de dados da DBCar.")
    @Response
    @PostMapping
    public ResponseEntity<FuncionarioDTO> create(@RequestBody @Valid FuncionarioCreateDTO funcionario) throws RegraDeNegocioException {
        return new ResponseEntity<>(funcionarioService.create(funcionario), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar dados de um funcionário", description = "Atualizará todos os dados de um funcionário do banco de dados da DBCar.")
    @Response
    @PutMapping("/{idFuncionario}")
    public ResponseEntity<FuncionarioDTO> update(@PathVariable("idFuncionario") Integer idFuncionario,
                                                 @RequestBody @Valid FuncionarioUpdateDTO funcionarioAtualizar) throws RegraDeNegocioException {
        return new ResponseEntity<>(funcionarioService.update(idFuncionario, funcionarioAtualizar), HttpStatus.OK);
    }

    @Operation(summary = "Remover funcionário do banco de dados", description = "Removerá o funcionário e todos os seus dados do banco de dados")
    @Response
    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<Void> delete(@PathVariable("idFuncionario") Integer idFuncionario) throws RegraDeNegocioException {
        funcionarioService.delete(idFuncionario);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
