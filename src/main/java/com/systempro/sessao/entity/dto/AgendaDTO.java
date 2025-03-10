package com.systempro.sessao.entity.dto;

import java.util.UUID;

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
public class AgendaDTO {

	private UUID id;
	@NotEmpty(message = "A descrição da pauta é obrigatória.")
	private String description;

}
