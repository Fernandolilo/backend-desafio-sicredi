package com.systempro.sessao.entity.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AssociatedDTO {

	private UUID id;
	@NotEmpty(message = "CPF é obriagatorio.")
	private String cpf;
	@NotEmpty(message = "A descrição da pauta é obrigatória.")
	private String nome;

}
