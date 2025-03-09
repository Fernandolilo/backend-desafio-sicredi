package com.systempro.sessao.entity.dto;

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

	
	@NotEmpty(message = "A descrição da pauta é obrigatória.")
	private String agenda;
	
}
