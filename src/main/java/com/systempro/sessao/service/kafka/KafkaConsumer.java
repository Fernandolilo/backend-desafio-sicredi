package com.systempro.sessao.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "meu-topico", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Mensagem recebida: " + record.value());
    }
}