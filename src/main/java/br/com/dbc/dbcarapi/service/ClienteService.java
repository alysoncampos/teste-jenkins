package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.dto.ClienteCreateDTO;
import br.com.dbc.dbcarapi.dto.ClienteDTO;
import br.com.dbc.dbcarapi.dto.ClienteUpdateDTO;
import br.com.dbc.dbcarapi.dto.UsuarioDTO;
import br.com.dbc.dbcarapi.entity.ClienteEntity;
import br.com.dbc.dbcarapi.entity.UsuarioEntity;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.repository.ClienteRepository;
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
public class ClienteService {

    private final ObjectMapper objectMapper;
    
    private final StandardPasswordEncoder standardPasswordEncoder = new StandardPasswordEncoder();

    private final ClienteRepository clienteRepository;

    private final CargoService cargoService;

    private final UsuarioService usuarioService;

    private final EmailService emailService;
    
    public List<ClienteDTO> list() throws RegraDeNegocioException {
        log.info("Listando clientes...");

        if (clienteRepository.findAll().isEmpty()) {
            throw new RegraDeNegocioException("Não foi possível realizar a listagem dos clientes.");
        } else {
            return clienteRepository.findAll().stream()
                    .map(clienteEntity -> {
                        ClienteDTO clienteDTO = convertClienteDTO(clienteEntity);
                        clienteDTO.setUsuario(objectMapper.convertValue(clienteEntity.getUsuarioEntity(),UsuarioDTO.class));
                        return clienteDTO;
                    })
                    .collect(Collectors.toList());
        }
    }

    public ClienteDTO findByIdCliente(Integer idCliente) throws RegraDeNegocioException {
        ClienteEntity clienteEntityRecuperado = buscaIdCliente(idCliente);

        ClienteDTO clienteDTO = convertClienteDTO(clienteEntityRecuperado);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(clienteEntityRecuperado.getUsuarioEntity(), UsuarioDTO.class);
        clienteDTO.setUsuario(usuarioDTO);

        return clienteDTO;
    }

    public ClienteEntity buscaIdCliente(Integer id) throws RegraDeNegocioException {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));
    }

    public ClienteDTO listInfosCliente() throws RegraDeNegocioException {
        Integer idUsuario = usuarioService.getIdLoggedUser();

        ClienteEntity clienteEntityRecuperado = findByIdUsuario(idUsuario);

        ClienteDTO clienteDTO = convertClienteDTO(clienteEntityRecuperado);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(clienteEntityRecuperado.getUsuarioEntity(), UsuarioDTO.class);
        clienteDTO.setUsuario(usuarioDTO);

        return clienteDTO;
    }
    
    public ClienteDTO create(ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException {
        log.info("Cadastrando novo cliente...");

        ClienteEntity clienteEntity = objectMapper.convertValue(clienteCreateDTO, ClienteEntity.class);
        UsuarioEntity usuarioEntity = objectMapper.convertValue(clienteCreateDTO.getUsuarioLogin(), UsuarioEntity.class);

        usuarioEntity.setSenha(criptografarSenha(usuarioEntity.getSenha()));
        usuarioEntity.setStatus(true);
        usuarioEntity.setCargos(Set.of(cargoService.findByCargo("ROLE_CLIENTE")));

        clienteEntity.setUsuarioEntity(usuarioEntity);

        clienteRepository.save(clienteEntity);

        ClienteDTO clienteDTO = convertClienteDTO(clienteEntity);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        clienteDTO.setUsuario(usuarioDTO);

        log.info("Cliente cadastrado com sucesso!");
        String tipo = "create";
        emailService.sendEmailNovoCliente(clienteDTO, tipo);

        return clienteDTO;
    }
    
    public ClienteDTO update(ClienteUpdateDTO clienteAtualizar) throws RegraDeNegocioException {
        log.info("Atualizando dados do cliente...");

        Integer idUsuario = usuarioService.getIdLoggedUser();

        ClienteEntity clienteEntityRecuperado = findByIdUsuario(idUsuario);
        ClienteEntity clienteEntityAtualizar = objectMapper.convertValue(clienteAtualizar, ClienteEntity.class);

        if(clienteAtualizar.getNome() == null){
            clienteEntityAtualizar.setNome(clienteEntityRecuperado.getNome());
        }
        if(clienteAtualizar.getDataNascimento() == null){
            clienteEntityAtualizar.setDataNascimento(clienteEntityRecuperado.getDataNascimento());
        }
        if(clienteAtualizar.getCpf() == null){
            clienteEntityAtualizar.setCpf(clienteEntityRecuperado.getCpf());
        }
        if(clienteAtualizar.getTelefone() == null){
            clienteEntityAtualizar.setTelefone(clienteEntityRecuperado.getTelefone());
        }
        if(clienteAtualizar.getEmail() == null){
            clienteEntityAtualizar.setEmail(clienteEntityRecuperado.getEmail());
        }
        clienteEntityAtualizar.setIdCliente(clienteEntityRecuperado.getIdCliente());
        clienteEntityAtualizar.setUsuarioEntity(clienteEntityRecuperado.getUsuarioEntity());

        ClienteDTO clienteDTO = convertClienteDTO(clienteRepository.save(clienteEntityAtualizar));
        UsuarioDTO usuarioDTO =
                objectMapper.convertValue(clienteEntityRecuperado.getUsuarioEntity(), UsuarioDTO.class);
        clienteDTO.setUsuario(usuarioDTO);

        log.info("Dados do cliente foram atualizados com sucesso!");

        String tipo = "update";
        emailService.sendEmailNovoCliente(clienteDTO, tipo);

        return clienteDTO;
    }
    
    public void delete(Integer idCliente) throws RegraDeNegocioException {
        log.info("Removendo dados do cliente...");

        Integer idUsuario = usuarioService.getIdLoggedUser();
        ClienteEntity clienteEntity = buscaIdCliente(idCliente);

        if (clienteEntity.getIdUsuario().equals(idUsuario)) {
            if(clienteEntity.getAlugueis().isEmpty()) {
                clienteRepository.delete(clienteEntity);
                log.info("Cliente removido com sucesso.");
            } else {
                throw new RegraDeNegocioException("Não é possível remover cliente com histórico de alugueis");
            }
        }
    }

    public ClienteEntity findByIdUsuario(Integer idUsuario) throws RegraDeNegocioException {
        return clienteRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não enontrado"));
    }
    
    private ClienteEntity convertClienteEntity(ClienteCreateDTO cliente) {
        return objectMapper.convertValue(cliente, ClienteEntity.class);
    }
    
    private String criptografarSenha(String senha) {
        String senhaEnconder = standardPasswordEncoder.encode(senha);
        return senhaEnconder;
    }
    
    private ClienteDTO convertClienteDTO(ClienteEntity clienteEntity) {
        return objectMapper.convertValue(clienteEntity, ClienteDTO.class);
    }
}
