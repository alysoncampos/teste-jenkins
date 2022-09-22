package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.dto.*;
import br.com.dbc.dbcarapi.entity.FuncionarioEntity;
import br.com.dbc.dbcarapi.entity.UsuarioEntity;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.repository.FuncionarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FuncionarioService {

    private final ObjectMapper objectMapper;

    private final FuncionarioRepository funcionarioRepository;
    
    private final StandardPasswordEncoder standardPasswordEncoder = new StandardPasswordEncoder();

    private final CargoService cargoService;

    public List<FuncionarioDTO> list() throws RegraDeNegocioException {
        log.info("Listando os funcionários da DBCar...");

        if (funcionarioRepository.findAll().isEmpty()) {
            throw new RegraDeNegocioException("Nenhum funcionário cadastrado.");
        } else {
            return funcionarioRepository.findAll().stream()
                    .map(funcionarioEntity -> {
                        FuncionarioDTO funcionarioDTO = funcionarioEntityToDTO(funcionarioEntity);
                        funcionarioDTO.setUsuario(objectMapper.convertValue(funcionarioEntity.getUsuarioEntity(),UsuarioDTO.class));
                        return funcionarioDTO;
                    })
                    .collect(Collectors.toList());
        }
    }
    
    public List<FuncionarioRespDTO> listParaAluguel() throws RegraDeNegocioException {
        log.info("Listando os funcionários da DBCar...");
        
        if (funcionarioRepository.findAll().isEmpty()) {
            throw new RegraDeNegocioException("Nenhum funcionário cadastrado.");
        } else {
            return funcionarioRepository.findAll().stream()
                    .map(funcionarioEntity -> funcionarioEntityToRespDTO(funcionarioEntity))
                    .collect(Collectors.toList());
        }
    }

    public FuncionarioDTO listByIdFuncionario(Integer idFuncionario) throws RegraDeNegocioException {
        FuncionarioEntity funcionarioEntityRecuperado = findByIdFuncionario(idFuncionario);

        FuncionarioDTO funcionarioDTO = funcionarioEntityToDTO(funcionarioEntityRecuperado);
        UsuarioDTO usuarioDTO =
                objectMapper.convertValue(funcionarioEntityRecuperado.getUsuarioEntity(), UsuarioDTO.class);
        funcionarioDTO.setUsuario(usuarioDTO);

        return funcionarioDTO;
    }

    public FuncionarioEntity findByIdFuncionario(Integer idFuncionario) throws RegraDeNegocioException {
        return funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RegraDeNegocioException("Funcionário não encontrado."));
    }

    public FuncionarioDTO create(FuncionarioCreateDTO funcionarioCreateDTO) throws RegraDeNegocioException {
        log.info("Adicionando um novo funcionário ao sistema da DBCar...");

        FuncionarioEntity funcionarioEntity = funcionarioDTOToEntity(funcionarioCreateDTO);
        UsuarioEntity usuarioEntity = objectMapper.convertValue(funcionarioCreateDTO.getUsuarioLogin(), UsuarioEntity.class);

        usuarioEntity.setSenha(criptografarSenha(usuarioEntity.getSenha()));
        usuarioEntity.setStatus(true);
        usuarioEntity.setCargos(Set.of(cargoService.findByCargo("ROLE_FUNCIONARIO")));

        funcionarioEntity.setComissao(0.0);
        funcionarioEntity.setUsuarioEntity(usuarioEntity);

        funcionarioRepository.save(funcionarioEntity);

        FuncionarioDTO funcionarioDTO = funcionarioEntityToDTO(funcionarioEntity);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        funcionarioDTO.setUsuario(usuarioDTO);

        log.info("O funcionário foi adicionado com sucesso.");

        return funcionarioDTO;
    }

    public FuncionarioDTO update(Integer idFuncionario, FuncionarioUpdateDTO funcionarioAtualizar) throws RegraDeNegocioException {
        log.info("Atualizando os dados do funcionário...");

        FuncionarioEntity funcionarioEntityRecuperado = findByIdFuncionario(idFuncionario);
        FuncionarioEntity funcionarioEntityAtualizar = objectMapper.convertValue(funcionarioAtualizar, FuncionarioEntity.class);

        if(funcionarioAtualizar.getNome() == null){
            funcionarioEntityAtualizar.setNome(funcionarioEntityRecuperado.getNome());
        }
        if(funcionarioAtualizar.getMatricula() == null){
            funcionarioEntityAtualizar.setMatricula(funcionarioEntityRecuperado.getMatricula());
        }
        if(funcionarioAtualizar.getSalario() == null){
            funcionarioEntityAtualizar.setSalario(funcionarioEntityRecuperado.getSalario());
        }
        funcionarioEntityAtualizar.setIdFuncionario(idFuncionario);
        funcionarioEntityAtualizar.setComissao(funcionarioEntityRecuperado.getComissao());
        funcionarioEntityAtualizar.setUsuarioEntity(funcionarioEntityRecuperado.getUsuarioEntity());

        FuncionarioDTO funcionarioDTO = funcionarioEntityToDTO(funcionarioRepository.save(funcionarioEntityAtualizar));
        UsuarioDTO usuarioDTO =
                objectMapper.convertValue(funcionarioEntityRecuperado.getUsuarioEntity(), UsuarioDTO.class);
        funcionarioDTO.setUsuario(usuarioDTO);

        log.info("Os dados do funcionário foram atualizados com sucesso!");

        return funcionarioDTO;
    }

    public void delete(Integer idFuncionario) throws RegraDeNegocioException {
        log.info("Removendo funcionário da DBCar...");
        funcionarioRepository.delete(findByIdFuncionario(idFuncionario));
        log.info("O funcionário foi removido com sucesso!");
    }

    public FuncionarioDTO funcionarioEntityToDTO(FuncionarioEntity funcionarioEntity) {
        return objectMapper.convertValue(funcionarioEntity, FuncionarioDTO.class);
    }
    
    public FuncionarioRespDTO funcionarioEntityToRespDTO(FuncionarioEntity funcionarioEntity) {
        return objectMapper.convertValue(funcionarioEntity, FuncionarioRespDTO.class);
    }

    public FuncionarioEntity funcionarioDTOToEntity(FuncionarioCreateDTO funcionarioCreateDTO) {
        return objectMapper.convertValue(funcionarioCreateDTO, FuncionarioEntity.class);
    }
    
    private String criptografarSenha(String senha) {
        String senhaEnconder = standardPasswordEncoder.encode(senha);
        return senhaEnconder;
    }
}
