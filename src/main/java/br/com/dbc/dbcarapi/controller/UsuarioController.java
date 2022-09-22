package br.com.dbc.dbcarapi.controller;

import br.com.dbc.dbcarapi.config.Response;
import br.com.dbc.dbcarapi.dto.RecuperarSenhaDTO;
import br.com.dbc.dbcarapi.dto.UsuarioDTO;
import br.com.dbc.dbcarapi.dto.UsuarioLoginDTO;
import br.com.dbc.dbcarapi.entity.UsuarioEntity;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.security.TokenService;
import br.com.dbc.dbcarapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Validated
public class UsuarioController {
    
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    
    @Response
    @PostMapping("/login")
    public String login(@RequestBody @Valid UsuarioLoginDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.getLogin(),
                        login.getSenha()
                );
        
        Authentication authentication =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        
        Object usuarioLogado = authentication.getPrincipal();
        UsuarioEntity usuarioEntity = (UsuarioEntity) usuarioLogado;

        String token = tokenService.getToken(usuarioEntity);
        return token;
    }
    
    @Operation(summary = "Exibir usuario logado")
    @Response
    @GetMapping("/logado")
    public ResponseEntity<UsuarioDTO> getLoggedUser() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }
    
    @Operation(summary = "Cadastrar administrador")
    @Response
    @PostMapping("/cadastrar-admin")
    public ResponseEntity<UsuarioDTO> cadastrarAdministrador(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.cadastrarAdministrador(usuarioLoginDTO), HttpStatus.CREATED);
    }
    
    @Operation(summary = "Atualizar dados do usuário.")
    @Response
    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.atualizarUsuario(usuarioLoginDTO), HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "Alterar senha de usuário")
    @Response
    @PutMapping("/atualizar-senha")
    public ResponseEntity<UsuarioDTO> atualizarSenha(@RequestBody @Valid RecuperarSenhaDTO recuperarSenhaDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.atualizarSenha(recuperarSenhaDTO), HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "Ativar/Desativar usuário.")
    @Response
    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.ativarDesativarUsuario(idUsuario), HttpStatus.OK);
    }
}
