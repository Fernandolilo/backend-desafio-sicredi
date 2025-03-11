/*package com.systempro.sessao.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.dto.VoteNewDTO;

@Service
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private VoteNewDTO vote;

     Método de consumo da mensagem do Kafka
    @KafkaListener(topics = "order-vote-popule", groupId = "api-consumer-group")
    public void consume(String message) {
        try {
            System.out.println("Mensagem recebida do Kafka: " + message);
            vote = objectMapper.readValue(message, VoteNewDTO.class);
            System.out.println("Voto desserializado: " + vote);
        } catch (Exception e) {
            System.err.println("Erro ao desserializar a mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

     Método que retorna o último voto recebido
    public VoteNewDTO getLastVote() {
        return vote;
    }

    // Método para criar um novo objeto VoteNewDTO baseado no último voto recebido
    public VoteNewDTO createVoteDTO() {
        if (vote != null) {
            return VoteNewDTO.builder()
                    .id_associate(vote.getId_associate())
                    .id_session(vote.getId_session())
                    .build();
        }
        return null; // Retorna null se não houver voto
    }
}
*/

package com.systempro.sessao.service.kafka;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.dto.VoteNewDTO;

@Service
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<VoteNewDTO> voteList = new ArrayList<>();

    @KafkaListener(topics = "order-vote-popule", groupId = "api-consumer-group")
    public void consume(String message) {
        try {
            System.out.println("Mensagem recebida do Kafka: " + message);
            VoteNewDTO vote = objectMapper.readValue(message, VoteNewDTO.class);
            voteList.add(vote); // Armazena os votos recebidos na lista
            System.out.println("Voto armazenado na lista: " + vote);
        } catch (Exception e) {
            System.err.println("Erro ao desserializar a mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Retorna a lista completa de votos recebidos
    public List<VoteNewDTO> getVoteList() {
        return new ArrayList<>(voteList);
    }

    // Limpa a lista de votos processados
    public void clearVoteList() {
        voteList.clear();
    }
}
