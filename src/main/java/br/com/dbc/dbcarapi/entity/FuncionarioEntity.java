package br.com.dbc.dbcarapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "funcionario")
public class FuncionarioEntity {

    @Id
    @SequenceGenerator(name = "seq_funcionario", sequenceName = "seq_funcionario", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_funcionario")
    @Column(name = "id_funcionario")
    private Integer idFuncionario;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "salario")
    private Double salario;

    @Column(name = "comissao")
    private Double comissao;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "funcionarioEntity")
    private Set<AluguelEntity> alugueis;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuarioEntity;
}
