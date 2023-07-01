package com.dynss.cloudtecnologia.model.entity;


import com.dynss.cloudtecnologia.rest.dto.NaturezaDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;


@JsonIgnoreProperties(ignoreUnknown = true, value = {"usuario"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Natureza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false, length = 200)
    private String descricao;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public Natureza(NaturezaDTO dto, Usuario usuario) {
        this.descricao = dto.getDescricao();
        this.usuario = usuario;
    }
}
