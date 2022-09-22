package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.dto.*;
import br.com.dbc.dbcarapi.dto.AluguelCreateDTO;
import br.com.dbc.dbcarapi.dto.AluguelDTO;
import br.com.dbc.dbcarapi.dto.AluguelUpdateDTO;
import br.com.dbc.dbcarapi.dto.FuncionarioDTO;
import br.com.dbc.dbcarapi.dto.RelatorioAluguelDTO;
import br.com.dbc.dbcarapi.dto.CarroDTO;
import br.com.dbc.dbcarapi.dto.ClienteDTO;
import br.com.dbc.dbcarapi.entity.*;
import br.com.dbc.dbcarapi.enums.ClasseCarro;
import br.com.dbc.dbcarapi.enums.StatusAluguel;
import br.com.dbc.dbcarapi.enums.StatusCarro;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.repository.AluguelRepository;
import br.com.dbc.dbcarapi.repository.ClienteRepository;
import br.com.dbc.dbcarapi.repository.FuncionarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AluguelService {

    private final ObjectMapper objectMapper;

    private final AluguelRepository aluguelRepository;

    private final CarroService carroService;

    private final ClienteRepository clienteRepository;

    private final FuncionarioService funcionarioService;

    private final UsuarioService usuarioService;

    private final FuncionarioRepository funcionarioRepository;

    public PageDTO<AluguelDTO> list(Integer pagina, Integer registro) throws RegraDeNegocioException {
        log.info("Listando alugueis...");
        if (aluguelRepository.findAll().isEmpty()) {
            throw new RegraDeNegocioException("Não há nenhum aluguel registrado.");
        } else {
            PageRequest pageRequest = PageRequest.of(pagina, registro);
            Page<AluguelEntity> page = aluguelRepository.findAll(pageRequest);
            List<AluguelDTO> alugueisDTO = page.getContent().stream()
                    .map(aluguelEntity -> {
                        AluguelDTO aluguelDTO = aluguelCompletoToDTO(aluguelEntity);
                        aluguelDTO.setDataAluguel(aluguelEntity.getDataAluguel());
                        aluguelDTO.setDataEntrega(aluguelEntity.getDataEntrega());

                        return aluguelDTO;
                    }).collect(Collectors.toList());
            return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registro, alugueisDTO);
        }
    }

    public List<RelatorioAluguelDTO> listRelatorioAluguel(Integer idAluguel) throws RegraDeNegocioException {
        if (aluguelRepository.listRelatorioAluguel(idAluguel).isEmpty()){
            throw new RegraDeNegocioException("Não foi possível gerar o relatório do aluguel.");
        } else {
            return aluguelRepository.listRelatorioAluguel(idAluguel);
        }
    }

    public AluguelEntity findByIdAluguel(Integer idAluguel) throws RegraDeNegocioException {
        return aluguelRepository.findById(idAluguel)
                .orElseThrow(() -> new RegraDeNegocioException("Aluguel não encontrado."));
    }

    public List<AluguelDTO> listAluguelByIdCliente() throws RegraDeNegocioException {
        Integer idUsuario = usuarioService.getIdLoggedUser();
        ClienteEntity clienteEntityRecuperado = findByIdUsuario(idUsuario);

        return aluguelRepository.findByCliente(clienteEntityRecuperado.getIdCliente(),StatusAluguel.ATIVO).stream()
                .map(aluguelEntity -> {
                    AluguelDTO aluguelDTO = aluguelClienteToDTO(aluguelEntity);
                    aluguelDTO.setDataAluguel(aluguelEntity.getDataAluguel());
                    aluguelDTO.setDataEntrega(aluguelEntity.getDataEntrega());

                    return aluguelDTO;
                }).collect(Collectors.toList());
    }

    public AluguelDTO create(Integer idCarro, AluguelCreateDTO aluguelCreateDTO) throws RegraDeNegocioException {
        log.info("Realizando o aluguel...");

        Integer idUsuario = usuarioService.getIdLoggedUser();

        ClienteEntity clienteEntityRecuperado = findByIdUsuario(idUsuario);
        FuncionarioEntity funcionarioRecuperado = funcionarioService.findByIdFuncionario(aluguelCreateDTO.getIdFuncionario());
        CarroEntity carroRecuperado = carroService.findByIdCarro(idCarro);

        AluguelEntity aluguelEntity = aluguelDTOToEntity(aluguelCreateDTO);

        aluguelEntity.setClienteEntity(clienteEntityRecuperado);
        aluguelEntity.setFuncionarioEntity(funcionarioRecuperado);
        aluguelEntity.setCarroEntity(carroRecuperado);

        if(carroRecuperado.getStatus().equals(StatusCarro.DISPONIVEL)) {

            aluguelEntity.setStatus(StatusAluguel.ATIVO);
            carroService.editarStatus(StatusCarro.INDISPONIVEL, idCarro);

            aluguelEntity.setValorTotal(calcularDiarias(aluguelEntity, carroRecuperado.getIdCarro()));
            aluguelEntity = aluguelRepository.save(aluguelEntity);

            Double valorComissao = calcularComissao(aluguelEntity.getValorTotal());
            funcionarioRecuperado.setComissao(valorComissao);
            funcionarioRepository.save(funcionarioRecuperado);

        } else {
            throw new RegraDeNegocioException("Carro indisponível para aluguel.");
        }

        AluguelDTO aluguelDTO = aluguelCompletoToDTO(aluguelRepository.save(aluguelEntity));

        log.info("O aluguel foi realizado com sucesso.");

        return aluguelDTO;
    }

    public double calcularComissao(Double valorTotal) {
        final Double percentual = 0.1;
        return valorTotal * percentual;
    }

    public AluguelDTO update(Integer idAluguel, AluguelUpdateDTO aluguelUpdateDTO) throws RegraDeNegocioException {
        log.info("Atualizando dados do aluguel...");

        Integer idUsuario = usuarioService.getIdLoggedUser();

        AluguelEntity aluguelRecuperado = findByIdAluguel(idAluguel);

        if(aluguelRecuperado.getClienteEntity().getIdUsuario().equals(idUsuario)){
            if(aluguelRecuperado.getStatus().equals(StatusAluguel.ATIVO)){
                aluguelRecuperado.setIdAluguel(idAluguel);
                aluguelRecuperado.setDataAluguel(aluguelUpdateDTO.getDataAluguel());
                aluguelRecuperado.setDataEntrega(aluguelUpdateDTO.getDataEntrega());

                Double valorTotal = calcularDiarias(aluguelRecuperado, aluguelRecuperado.getCarroEntity().getIdCarro());
                aluguelRecuperado.setValorTotal(valorTotal);

                Double valorComissao = calcularComissao(aluguelRecuperado.getValorTotal());
                aluguelRecuperado.getFuncionarioEntity().setComissao(valorComissao);
            } else {
                throw new RegraDeNegocioException("Aluguel com status inativo ou cancelado");
            }
        } else {
            throw new RegraDeNegocioException("Identificação de aluguel informada está incorreta, verifique seu contrato");
        }


        AluguelDTO aluguelDTO = aluguelCompletoToDTO(aluguelRepository.save(aluguelRecuperado));

        log.info("Dados do aluguel atualizados com sucesso!");

        return aluguelDTO;
    }

    public AluguelDTO devolverCarro(Integer idAluguel) throws RegraDeNegocioException {
        log.info("Finalizando aluguel...");

        AluguelEntity aluguelRecuperado = findByIdAluguel(idAluguel);

        if(aluguelRecuperado.getStatus().equals(StatusAluguel.ATIVO)){
            aluguelRecuperado.setStatus(StatusAluguel.INATIVO);
            carroService.editarStatus(StatusCarro.DISPONIVEL, aluguelRecuperado.getCarroEntity().getIdCarro());
        } else {
            throw new RegraDeNegocioException("O aluguel já foi finzalizado");
        }

        AluguelDTO aluguelDTO = aluguelCompletoToDTO(aluguelRepository.save(aluguelRecuperado));

        log.info("Aluguel finalizado com sucesso!");

        return aluguelDTO;
    }

    public void cancelarAluguel(Integer idAluguel) throws RegraDeNegocioException {
        log.info("Cancelando aluguel...");

        AluguelEntity aluguelEntity = findByIdAluguel(idAluguel);

        if(aluguelEntity.getStatus().equals(StatusAluguel.ATIVO)){
            aluguelEntity.setStatus(StatusAluguel.CANCELADO);
            aluguelEntity.setValorTotal(0.0);
            carroService.editarStatus(StatusCarro.DISPONIVEL, aluguelEntity.getCarroEntity().getIdCarro());
            aluguelRepository.save(aluguelEntity);
            log.info("O aluguel foi cancelado com sucesso!");
        } else {
            throw new RegraDeNegocioException("O aluguel está inativo ou já foi cancelado");
        }
    }

    private Double calcularDiarias(AluguelEntity aluguelEntity, Integer idCarro) throws RegraDeNegocioException {

        LocalDate d1 = LocalDate.parse(aluguelEntity.getDataAluguel().toString(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate d2 = LocalDate.parse(aluguelEntity.getDataEntrega().toString(), DateTimeFormatter.ISO_LOCAL_DATE);

        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());

        long diffDays = diff.toDays();

        CarroEntity carroEntity = carroService.findByIdCarro(idCarro);

        if (carroEntity.getClasse().equals(ClasseCarro.A)) {
            return diffDays * carroEntity.getPrecoDiaria() * 0.5;
        } else if (carroEntity.getClasse().equals(ClasseCarro.B)) {
            return diffDays * carroEntity.getPrecoDiaria() * 0.2;
        } else {
            return diffDays * carroEntity.getPrecoDiaria();
        }
    }

    public ClienteEntity findByIdUsuario(Integer idUsuario) throws RegraDeNegocioException {
        return clienteRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não enontrado"));
    }

    public AluguelEntity aluguelDTOToEntity(AluguelCreateDTO aluguelCreateDTO) {
        return objectMapper.convertValue(aluguelCreateDTO, AluguelEntity.class);
    }

    public AluguelDTO aluguelEntityToDTO(AluguelEntity aluguelEntity) {
        return objectMapper.convertValue(aluguelEntity, AluguelDTO.class);
    }

    public AluguelDTO aluguelCompletoToDTO(AluguelEntity aluguelRecuperado) {
        AluguelDTO aluguelDTO = aluguelEntityToDTO(aluguelRecuperado);

        aluguelDTO.setCarro(objectMapper.convertValue(aluguelRecuperado.getCarroEntity(), CarroDTO.class));
        aluguelDTO.setFuncionario(objectMapper.convertValue(aluguelRecuperado.getFuncionarioEntity(), FuncionarioDTO.class));
        aluguelDTO.setCliente(objectMapper.convertValue(aluguelRecuperado.getClienteEntity(), ClienteDTO.class));

        return aluguelDTO;
    }

    public AluguelDTO aluguelClienteToDTO(AluguelEntity aluguelRecuperado) {
        AluguelDTO aluguelDTO = aluguelEntityToDTO(aluguelRecuperado);

        aluguelDTO.setCarro(objectMapper.convertValue(aluguelRecuperado.getCarroEntity(), CarroDTO.class));
        aluguelDTO.setCliente(objectMapper.convertValue(aluguelRecuperado.getClienteEntity(), ClienteDTO.class));

        return aluguelDTO;
    }
}
