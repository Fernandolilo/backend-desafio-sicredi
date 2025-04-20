package com.systempro.sessao.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteCountsDTO {

	private long countSim;
	private long countNao;
}
