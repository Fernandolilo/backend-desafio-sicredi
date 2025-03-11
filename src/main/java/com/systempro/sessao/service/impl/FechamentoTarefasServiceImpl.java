/*package com.systempro.sessao.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.FechamentoTarefasService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;
import com.systempro.sessao.service.kafka.KafkaConsumer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FechamentoTarefasServiceImpl implements FechamentoTarefasService {

    private final SessionRepository repository;
    private final VotacaoService votacaoService;
    private final AssociatedService associatedService;
    private final SessionService sessionService;
    private final KafkaConsumer kafkaConsumer;
    private final ObjectMapper objectMapper;

    @Override
    @Scheduled(cron = "0 0/1 * 1/1 * ?") // Executa a cada 1 minuto
    public void saveVotos() {
        // Busca as sessões com status "FECHADO"
        List<Session> fechadas = repository.findByStatus(StatusEnum.FECHADO);

        if (fechadas != null && !fechadas.isEmpty()) {
            // Obtém a última mensagem do Kafka (último voto recebido)
            VoteNewDTO voteDTO = kafkaConsumer.getLastVote();
            if (voteDTO == null) {
                System.err.println("Nenhuma mensagem válida do Kafka.");
                return;
            }

            System.out.println("Voto recebido do Kafka: " + voteDTO);

			// Busca os objetos associados
			Optional<Associated> associated = associatedService.findById(voteDTO.getId_associate());
			Optional<Session> session = sessionService.findById(voteDTO.getId_session());

			if (associated.isPresent() && session.isPresent()) {
			    // Cria o objeto Vote para salvar
			    Vote voteNew = Vote.builder()
			            .associated(associated.get())
			            .session(session.get())
			            .vote(voteDTO.getVote()) // Garante que está pegando o voto correto
			            .build();

			    // Salva o voto usando o serviço de votação
			    votacaoService.save(voteNew);
			    System.out.println("Voto salvo com sucesso!");
			} else {
			    System.err.println("Erro: Associated ou Session não encontrados.");
			}
        } else {
            System.err.println("Nenhuma sessão fechada encontrada.");
        }
    }

    private String fetchVoteFromSomewhere() {
        // Método para buscar e converter o último voto para JSON
        VoteNewDTO voteDTO = kafkaConsumer.getLastVote(); // Obtém o último voto recebido
        if (voteDTO == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(voteDTO); // Converte o objeto VoteNewDTO para JSON
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao converter VoteNewDTO para JSON: " + e.getMessage());
            return null;
        }
    }
}
*/

package com.systempro.sessao.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.FechamentoTarefasService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;
import com.systempro.sessao.service.kafka.KafkaConsumer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FechamentoTarefasServiceImpl implements FechamentoTarefasService {

    private final SessionRepository repository;
    private final VotacaoService votacaoService;
    private final AssociatedService associatedService;
    private final SessionService sessionService;
    private final KafkaConsumer kafkaConsumer;

    @Override
    @Scheduled(cron = "0 0/1 * * * ?") // Executa a cada 1 minuto
    public void saveVotos() {
        List<Session> fechadas = repository.findByStatus(StatusEnum.FECHADO);

        if (fechadas == null || fechadas.isEmpty()) {
            System.out.println("Nenhuma sessão fechada encontrada.");
            return;
        }

        List<VoteNewDTO> voteList = kafkaConsumer.getVoteList(); // Busca todos os votos armazenados
        if (voteList.isEmpty()) {
            System.out.println("Nenhum voto pendente encontrado.");
            return;
        }

        for (VoteNewDTO voteDTO : voteList) {
            try {
                System.out.println("Processando voto do Kafka: " + voteDTO);

                Optional<Associated> associated = associatedService.findById(voteDTO.getId_associate());
                Optional<Session> session = sessionService.findById(voteDTO.getId_session());

                if (associated.isPresent() && session.isPresent()) {
                    Vote voteNew = Vote.builder()
                            .associated(associated.get())
                            .session(session.get())
                            .vote(voteDTO.getVote())
                            .build();

                    votacaoService.save(voteNew);
                    System.out.println("Voto salvo com sucesso: " + voteNew);
                } else {
                    System.err.println("Erro: Associated ou Session não encontrados para voto: " + voteDTO);
                }

            } catch (Exception e) {
                System.err.println("Erro ao processar voto: " + e.getMessage());
                e.printStackTrace();
            }
        }

        kafkaConsumer.clearVoteList(); // Limpa a lista de votos após o processamento
        System.out.println("Todos os votos foram processados e salvos.");
    }
}
