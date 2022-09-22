package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.dto.RecuperarSenhaDTO;
import br.com.dbc.dbcarapi.dto.UsuarioDTO;
import br.com.dbc.dbcarapi.dto.UsuarioLoginDTO;
import br.com.dbc.dbcarapi.entity.UsuarioEntity;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    
    private final ObjectMapper objectMapper;

    private final CargoService cargoService;
    
    private final StandardPasswordEncoder standardPasswordEncoder = new StandardPasswordEncoder();
    
    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));
    }
    
    public Integer getIdLoggedUser() {
        Integer findUserId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findUserId;
    }
    
    private String criptografarSenha(String senha) {
        String senhaEnconder = standardPasswordEncoder.encode(senha);
        return senhaEnconder;
    }
    
    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {
        return usuarioEntityToDTO(findById(getIdLoggedUser()));
    }

    public UsuarioDTO cadastrarAdministrador(UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {

        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioLoginDTO, UsuarioEntity.class);

        usuarioEntity.setStatus(true);
        usuarioEntity.setSenha(criptografarSenha(usuarioEntity.getSenha()));
        usuarioEntity.setCargos(Set.of(cargoService.findByCargo("ROLE_ADMIN")));

        return objectMapper.convertValue(usuarioRepository.save(usuarioEntity), UsuarioDTO.class);
    }
    
    public UsuarioDTO atualizarSenha(RecuperarSenhaDTO recuperarSenhaDTO) throws RegraDeNegocioException {
        
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntity = findById(idUsuario);
        usuarioEntity.setSenha(criptografarSenha(recuperarSenhaDTO.getSenha()));
        
        usuarioRepository.save(usuarioEntity);
        
        return usuarioEntityToDTO(usuarioEntity);
    }
    
    public UsuarioDTO atualizarUsuario(UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntity = findById(idUsuario);
        
        if (usuarioLoginDTO.getLogin() != null) {
            usuarioEntity.setLogin(usuarioLoginDTO.getLogin());
        }
        if (usuarioLoginDTO.getSenha() != null) {
            usuarioEntity.setSenha(criptografarSenha(usuarioLoginDTO.getSenha()));
        }
        
        usuarioRepository.save(usuarioEntity);
        
        return usuarioEntityToDTO(usuarioEntity);
    }
    
    public String ativarDesativarUsuario(Integer idUsuario) throws RegraDeNegocioException {

        UsuarioEntity usuarioEntity = findById(idUsuario);

        if (usuarioEntity.getStatus()) {
            usuarioEntity.setStatus(false);
            usuarioRepository.save(usuarioEntity);
            return "USUÁRIO DESATIVADO";
        } else {
            usuarioEntity.setStatus(true);
            usuarioRepository.save(usuarioEntity);
            return "USUÁRIO ATIVADO";
        }
    }

    public UsuarioDTO usuarioEntityToDTO(UsuarioEntity usuarioEntity) {
        return objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
    }
    
    public Optional<UsuarioEntity> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }
}
