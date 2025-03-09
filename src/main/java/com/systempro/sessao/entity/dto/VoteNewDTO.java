package com.systempro.sessao.entity.dto;

import java.util.UUID;

import com.systempro.sessao.enuns.VoteEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteNewDTO {
	
	
	private VoteEnum vote;	
	private UUID id_session;
	private UUID id_associate;

}
