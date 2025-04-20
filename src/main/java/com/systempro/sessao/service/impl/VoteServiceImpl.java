package com.systempro.sessao.service.impl;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.VoteCountsDTO;
import com.systempro.sessao.enuns.VoteEnum;
import com.systempro.sessao.repository.VoteRepository;
import com.systempro.sessao.service.VotacaoService;

@Service
public class VoteServiceImpl implements VotacaoService {

	private final VoteRepository repository;
	private final KafkaTemplate<String, String> kafkaTemplate;

	public VoteServiceImpl(VoteRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
		this.repository = repository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public Vote save(Vote vote) {
		return repository.save(vote);
	}
	
	@Override
    public VoteCountsDTO countVotesBySession(UUID sessionId) {
        long Sim = repository.countBySession_IdAndVote(sessionId, VoteEnum.SIM);
        long Nao = repository.countBySession_IdAndVote(sessionId, VoteEnum.NAO);
        return new VoteCountsDTO(Sim, Nao);
    }

	
}
