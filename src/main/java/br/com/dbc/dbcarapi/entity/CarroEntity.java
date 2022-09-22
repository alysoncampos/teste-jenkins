package br.com.dbc.dbcarapi.entity;

import br.com.dbc.dbcarapi.enums.StatusCarro;
import br.com.dbc.dbcarapi.enums.ClasseCarro;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "carro")
public class CarroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARRO")
    @SequenceGenerator(name = "SEQ_CARRO", sequenceName = "seq_carro", allocationSize = 1)
    @Column(name = "id_carro")
    private Integer idCarro;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private StatusCarro status;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "marca")
    private String marca;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "classe")
    private ClasseCarro classe;

    @Column(name = "placa")
    private String placa;

    @Column(name = "preco_diaria")
    private Double precoDiaria;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "carroEntity")
    private Set<AluguelEntity> alugueis;
}
