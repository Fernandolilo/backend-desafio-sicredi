package com.systempro.sessao.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "voto", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Mensagem recebida do Kafka: " + record.value());
    }
}

