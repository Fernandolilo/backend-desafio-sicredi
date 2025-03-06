package com.systempro.sessao.entity;

import java.util.UUID;
import com.systempro.sessao.enuns.VoteEnum;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    private VoteEnum vote;

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false) // Garante que não seja nulo
    private Session session;
}
