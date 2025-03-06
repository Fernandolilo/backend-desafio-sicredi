package com.systempro.sessao.entity;

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
public class Vote {


	private UUID id;	
	private VoteEnum vote;
	
	
}
