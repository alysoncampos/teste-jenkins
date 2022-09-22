package br.com.dbc.dbcarapi.repository;

import br.com.dbc.dbcarapi.dto.RelatorioCarroDTO;
import br.com.dbc.dbcarapi.entity.CarroEntity;
import br.com.dbc.dbcarapi.enums.ClasseCarro;
import br.com.dbc.dbcarapi.enums.StatusCarro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<CarroEntity, Integer> {

    @Query(value = " select new br.com.dbc.dbcarapi.dto.RelatorioCarroDTO( " +
            "        c.idCarro, " +
            "        c.status, " +
            "        c.modelo, " +
            "        c.marca, " +
            "        c.precoDiaria, " +
            "        a.dataAluguel, " +
            "        a.dataEntrega," +
            "        a.clienteEntity.nome, " +
            "        a.clienteEntity.cpf, " +
            "        a.clienteEntity.email " +
            ")" +
            "        from carro c " +
            "   left join c.alugueis a " +
            "       where (:idCarro is null or c.idCarro = :idCarro)")
    List<RelatorioCarroDTO> listRelatorioCarro(@Param("idCarro") Integer idCarro);

    @Query(" select c " +
            "  from carro c " +
            " where (:idCarro is null or c.idCarro = :idCarro) " +
            "   and (:classe is null or c.classe = :classe) " +
            "   and (:status is null or c.status = :status)")
    Page<CarroEntity> listInfoCarro(@Param("idCarro") Integer idCarro,
                                    @Param("classe") ClasseCarro classe,
                                    @Param("status") StatusCarro status,
                                    Pageable pageable);
}
