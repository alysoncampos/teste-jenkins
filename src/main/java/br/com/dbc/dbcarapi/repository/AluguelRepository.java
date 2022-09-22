package br.com.dbc.dbcarapi.repository;

import br.com.dbc.dbcarapi.dto.RelatorioAluguelDTO;
import br.com.dbc.dbcarapi.entity.AluguelEntity;
import br.com.dbc.dbcarapi.entity.ClienteEntity;
import br.com.dbc.dbcarapi.enums.StatusAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository<AluguelEntity, Integer> {

    @Query(value = " select new br.com.dbc.dbcarapi.dto.RelatorioAluguelDTO(" +
            "   a.idAluguel," +
            "   a.dataAluguel," +
            "   a.dataEntrega," +
            "   c.modelo," +
            "   c.marca," +
            "   c.classe," +
            "   c.placa," +
            "   c.precoDiaria," +
            "   a.valorTotal," +
            "   c.status," +
            "   f.nome," +
            "   f.matricula," +
            "   cliente.nome," +
            "   cliente.cpf," +
            "   cliente.telefone," +
            "   cliente.email " +
            ")" +
            "       from aluguel a" +
            "  left join a.carroEntity c " +
            "  left join a.funcionarioEntity f " +
            "  left join a.clienteEntity cliente" +
            "      where (:idAluguel is null or a.idAluguel = :idAluguel)")
    List<RelatorioAluguelDTO> listRelatorioAluguel(@Param("idAluguel") Integer idAluguel);

    @Query("  select a " +
            "   from aluguel a" +
            "  where (a.clienteEntity.idCliente = :idCliente)" +
            "    and (a.status = :status) ")
    List<AluguelEntity> findByCliente(@Param("idCliente") Integer idCliente,
                                      @Param("status") StatusAluguel status);
}
