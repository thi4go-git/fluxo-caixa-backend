package com.dynss.cloudtecnologia.model.entity;

import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private TipoLancamento tipo;

    @Column
    private String descricao;

    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;

    @Column(name = "valor_parcela")
    private BigDecimal valorParcela;

    @Column(name = "qtde_parcelas")
    private Integer qtdeParcelas;

    @Column(name = "nr_parcela")
    private Integer nrParcela;

    @ManyToOne
    @JoinColumn(name = "id_natureza")
    private Natureza natureza;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    Situacao situacao;

    @Column(name = "data_criacao", updatable = false)
    private LocalDate dataCriacao;

    @Column(name = "data_alteracao")
    private LocalDate dataAlteracao;

    @OneToOne
    @JoinColumn(name = "id_anexo")
    private Anexo anexo;
}
