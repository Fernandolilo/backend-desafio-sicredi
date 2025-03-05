package com.systempro.sessao.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendaNewDTO {

	@NotEmpty(message = "A descrição da pauta é obrigatória.")
	private String description;
}
