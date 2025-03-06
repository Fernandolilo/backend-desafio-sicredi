package com.systempro.sessao.entity.dto;

import java.util.UUID;

import com.systempro.sessao.enuns.VoteEnum;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteNewDTO {
	
	@NotEmpty(message = "O voto é obrigatorio.")
	private VoteEnum vote;
	@NotEmpty(message = "O Id é obrigatorio.")
	private UUID id_session;

}
