package com.systempro.sessao.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.Vote;

@Service
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order-vote-popule", groupId = "api-consumer-group")
    public void consume(String message) {
        try {
            Vote vote = objectMapper.readValue(message, Vote.class);
            System.out.println("Voto recebido: " + vote);
        } catch (Exception e) {
            System.err.println("Erro ao desserializar a mensagem: " + e.getMessage());
        }
    }
}
