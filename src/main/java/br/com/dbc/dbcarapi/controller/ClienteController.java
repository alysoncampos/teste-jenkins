package br.com.dbc.dbcarapi.controller;

import br.com.dbc.dbcarapi.config.Response;
import br.com.dbc.dbcarapi.dto.ClienteCreateDTO;
import br.com.dbc.dbcarapi.dto.ClienteDTO;
import br.com.dbc.dbcarapi.dto.ClienteUpdateDTO;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@Validated
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Listar todos os clientes", description = "Realizará a listagem de todos os clientes da DBCAR.")
    @Response
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(clienteService.list(), HttpStatus.OK);
    }

    @Operation(summary = "Listar cliente pelo ID", description = "Listara o cliente associado ao id e todos os seus dados.")
    @Response
    @GetMapping("/listar/{idCliente}")
    public ResponseEntity<ClienteDTO> listByIdCliente(Integer idCliente) throws RegraDeNegocioException {
        return new ResponseEntity<>(clienteService.findByIdCliente(idCliente), HttpStatus.OK);
    }

    @Operation(summary = "Listar informações do Cliente logado", description = "Listara o cliente associado ao id e todos os seus dados.")
    @Response
    @GetMapping("/infos-pessoais")
    public ResponseEntity<ClienteDTO> listInfosCliente() throws RegraDeNegocioException {
        return new ResponseEntity<>(clienteService.listInfosCliente(), HttpStatus.OK);
    }

    @Operation(summary = "Adicionar um novo cliente", description = "Adicionará um novo cliente a DBCar.")
    @Response
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody @Valid ClienteCreateDTO cliente) throws RegraDeNegocioException {
        return new ResponseEntity<>(clienteService.create(cliente), HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar os dados de um cliente", description = "Atualizará os dados de um cliente associado ao ID.")
    @Response
    @PutMapping("/atualizar")
    public ResponseEntity<ClienteDTO> update(@RequestBody @Valid ClienteUpdateDTO clienteAtualizar) throws RegraDeNegocioException {
        return new ResponseEntity<>(clienteService.update(clienteAtualizar), HttpStatus.OK);
    }

    @Operation(summary = "Deletar cliente", description = "Removerá o cliente e todos os seus dados do banco de dados da DBCar.")
    @Response
    @DeleteMapping("/{idCliente}")
    public ResponseEntity<Void> delete(@PathVariable("idCliente") Integer idCliente) throws RegraDeNegocioException {
        clienteService.delete(idCliente);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
