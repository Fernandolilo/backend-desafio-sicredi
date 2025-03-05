package com.systempro.sessao.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionNewDTO {

	@NotBlank(message = "A descrição da pauta é obrigatória.")
	@NotEmpty(message = "A descrição da pauta é obrigatória.")
	private String agenda;
}
