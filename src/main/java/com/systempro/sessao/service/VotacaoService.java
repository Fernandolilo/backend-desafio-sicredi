package com.systempro.sessao.service;

import com.systempro.sessao.entity.Vote;

public interface VotacaoService {

	Vote save(Vote any);
	
	void publicshVoteToKafka(Vote vote);

}
