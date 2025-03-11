package com.systempro.sessao.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.systempro.sessao.enuns.VoteEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//S@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos desconhecidos
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class) // Converte UUID para String no JSON
    @JsonDeserialize(using = com.fasterxml.jackson.databind.deser.std.UUIDDeserializer.class) // Converte String para UUID na desserialização
    private UUID id;

    @Enumerated(EnumType.STRING)
    private VoteEnum vote;

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "id_associated", nullable = false)
    private Associated associated;
}
