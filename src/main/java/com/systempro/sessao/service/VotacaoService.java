package com.systempro.sessao.service;

import java.util.UUID;

import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.VoteCountsDTO;

public interface VotacaoService {

	Vote save(Vote any);

	VoteCountsDTO countVotesBySession(UUID sessionId);
	
	

}
