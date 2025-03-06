package com.systempro.sessao.entity.dto;

import java.util.UUID;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.VoteEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {

	
	private UUID id;
	//private Session session;
	private VoteEnum vote;
	
	
}
