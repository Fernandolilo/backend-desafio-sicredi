package com.systempro.sessao.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.systempro.sessao.enuns.StatusEnum;

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
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private LocalDateTime inicio;
	private LocalDateTime fim;

	@JoinColumn(name = "id_agenda")
	@ManyToOne
	private Agenda agenda;
	@Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
	private StatusEnum status;

}
