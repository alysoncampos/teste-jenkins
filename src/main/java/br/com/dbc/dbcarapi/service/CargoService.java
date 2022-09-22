package br.com.dbc.dbcarapi.service;

import br.com.dbc.dbcarapi.entity.CargoEntity;
import br.com.dbc.dbcarapi.exception.RegraDeNegocioException;
import br.com.dbc.dbcarapi.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;

    public CargoEntity findByCargo(String cargo) throws RegraDeNegocioException {
        return cargoRepository.findByNome(cargo)
                .orElseThrow(() -> new RegraDeNegocioException("Cargo não encontrado."));
    }
}
