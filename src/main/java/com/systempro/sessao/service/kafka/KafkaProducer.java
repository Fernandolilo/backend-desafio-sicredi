package com.systempro.sessao.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.Vote;

@Service
public class KafkaProducer {

    private static final String TOPIC = "order-vote-popule";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Jackson para converter em JSON

    public void sendVote(Vote vote) {
        try {
            String voteJson = objectMapper.writeValueAsString(vote);
            kafkaTemplate.send(TOPIC, voteJson);
            System.out.println("Mensagem enviada para Kafka: " + voteJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar o voto para JSON", e);
        }
    }
}
