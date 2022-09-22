package br.com.dbc.dbcarapi.entity;

import br.com.dbc.dbcarapi.enums.StatusAluguel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "aluguel")
public class AluguelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ALUGUEL")
    @SequenceGenerator(name = "SEQ_ALUGUEL", sequenceName = "seq_aluguel", allocationSize = 1)
    @Column(name = "id_aluguel")
    private Integer idAluguel;

    @Column(name = "data_aluguel")
    private LocalDate dataAluguel;

    @Column(name = "data_entrega")
    private LocalDate dataEntrega;

    @Column(name = "valor_total")
    private Double valorTotal;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_aluguel")
    private StatusAluguel status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carro", referencedColumnName = "id_carro")
    private CarroEntity carroEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private ClienteEntity clienteEntity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id_funcionario")
    private FuncionarioEntity funcionarioEntity;
}
