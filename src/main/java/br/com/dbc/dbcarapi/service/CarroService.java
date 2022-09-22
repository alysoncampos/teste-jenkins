package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.dto.*;
import br.com.dbc.dbcarapi.dto.CarroCreateDTO;
import br.com.dbc.dbcarapi.dto.CarroDTO;
import br.com.dbc.dbcarapi.dto.RelatorioCarroDTO;
import br.com.dbc.dbcarapi.entity.CarroEntity;
import br.com.dbc.dbcarapi.enums.ClasseCarro;
import br.com.dbc.dbcarapi.enums.StatusCarro;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.repository.CarroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarroService {

    private final ObjectMapper objectMapper;

    private final CarroRepository carroRepository;

    public PageDTO<CarroDTO> listInfoCarro(Integer idCarro, ClasseCarro classe, StatusCarro status, Integer pagina, Integer registro) {
        PageRequest pageRequest = PageRequest.of(pagina, registro);
        Page<CarroEntity> page = carroRepository.listInfoCarro(idCarro, classe, status, pageRequest);
        List<CarroDTO> carroDTOS = page.getContent().stream()
                .map(carroEntity -> objectMapper.convertValue(carroEntity, CarroDTO.class))
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registro, carroDTOS);
    }

    public List<RelatorioCarroDTO> listRelatorioCarro(Integer idCarro) throws RegraDeNegocioException {
        if (carroRepository.listRelatorioCarro(idCarro).isEmpty()) {
            throw new RegraDeNegocioException("Nenhum relatório foi gerado.");
        } else {
            return carroRepository.listRelatorioCarro(idCarro);
        }
    }
    
    public CarroDTO create(CarroCreateDTO carro) throws RegraDeNegocioException {
        log.info("Adicionando novo carro...");
        
        CarroEntity carroEntity = objectMapper.convertValue(carro, CarroEntity.class);
        carroEntity.setStatus(StatusCarro.DISPONIVEL);
        carroRepository.save(carroEntity);
        
        log.info("O novo carro foi adicionado com sucesso.");
        return objectMapper.convertValue(carroEntity, CarroDTO.class);
    }
    
    public CarroDTO update(Integer idCarro, CarroCreateDTO carroAtualizar) throws RegraDeNegocioException {
        CarroEntity carroRecuperado = findByIdCarro(idCarro);

        CarroEntity carroEntity = objectMapper.convertValue(carroAtualizar, CarroEntity.class);
        carroEntity.setIdCarro(idCarro);
        carroEntity.setStatus(carroRecuperado.getStatus());
        carroEntity.setAlugueis(carroRecuperado.getAlugueis());

        log.info("Atualizando dados do carro...");

        CarroDTO carroDTO = objectMapper.convertValue(carroRepository.save(carroEntity), CarroDTO.class);

        log.info("Dados do carro atualizados");
        
        return carroDTO;
    }
    
    public void delete(Integer idCarro) throws RegraDeNegocioException {
        log.info("Deletando carro do catálogo...");
        
        CarroEntity carroRecuperado = findByIdCarro(idCarro);
        if (carroRecuperado.getStatus().equals(StatusCarro.DISPONIVEL)) {
            carroRepository.delete(carroRecuperado);
            log.info("O carro foi removido do catálogo com sucesso!");
        } else {
            throw new RegraDeNegocioException("Você não pode remover carro alugado");
        }
    }
    
    public CarroEntity findByIdCarro(Integer idCarro) throws RegraDeNegocioException {
        return carroRepository.findById(idCarro)
                .orElseThrow(() -> new RegraDeNegocioException("Carro não encontrado"));
    }

    public void editarStatus(StatusCarro status, Integer idCarro) throws RegraDeNegocioException {
        CarroEntity carro = findByIdCarro(idCarro);
        carro.setStatus(status);
        carroRepository.save(carro);
    }
}
