package com.systempro.sessao.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Vote;
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
	public void publicshVoteToKafka(Vote vote) {
		String message = "ID: " + vote.getId() + "Voto" + vote.getVote();
		kafkaTemplate.send("votes-topic", message);
	}
}
