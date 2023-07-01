package com.dynss.cloudtecnologia.model.entity;

import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.rest.dto.LancamentoDTO;
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

    @Column
    private LocalDate data_lancamento;

    @Column
    private BigDecimal valor_parcela;

    @Column
    private Integer qtde_parcelas;

    @Column
    private Integer nr_parcela;

    @ManyToOne
    @JoinColumn(name = "id_natureza")
    private Natureza natureza;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Situacao situacao;


    public Lancamento(LancamentoDTO dto, Integer nr_parcela, Usuario user,
                      BigDecimal valor_parcela, LocalDate data_lancamento, Natureza natureza) {
        this.tipo = dto.getTipo();
        this.descricao = dto.getDescricao();
        this.data_lancamento = data_lancamento;
        this.valor_parcela = valor_parcela;
        this.qtde_parcelas = dto.getQtde_parcelas();
        this.nr_parcela = nr_parcela;
        this.natureza = natureza;
        this.usuario = user;
        this.situacao = Situacao.EM_ABERTO;

    }

}
