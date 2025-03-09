package com.systempro.sessao.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Vote;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	private final KafkaTemplate<String, Vote> kafkaTemplate;
	
	public void sendVote(Vote vote) {
        kafkaTemplate.send("votacao", vote);
    }
}
