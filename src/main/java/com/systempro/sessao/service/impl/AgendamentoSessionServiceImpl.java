package com.systempro.sessao.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.enuns.VoteEnum;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AgendametnoSessionService;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AgendamentoSessionServiceImpl implements AgendametnoSessionService {

	private final SessionRepository repository;
	private final VotacaoService votacaoService;
	private final AssociatedService associatedService;
	private final SessionService sessionService;


	@Override
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void agendamentoTarefas() {
		// Busca as sessões com status "ABERTO"
		List<Session> abertas = repository.findByStatus(StatusEnum.ABERTO);

		// Filtra as sessões abertas que estão a mais de 1 minuto do início
		List<Session> sessionsFechadas = abertas.stream()
				.filter(s -> s.getInicio().isBefore(LocalDateTime.now().minusMinutes(1))).collect(Collectors.toList());

		// Fechar as sessões e atualizar o status para "FECHADO"
		sessionsFechadas.forEach(s -> {
			s.setFim(LocalDateTime.now());
			s.setStatus(StatusEnum.FECHADO);
			repository.save(s); // Salvar no banco
		});
	}

	@Override
	public void saveVotos() {
		VoteNewDTO voteDTO = VoteNewDTO.builder().build();
		Optional<Associated> associated = associatedService.findById(voteDTO.getId_associate());

		Optional<Session> session = sessionService.findById(voteDTO.getId_session());

		if (associated.isPresent() && session.isPresent()) {
			Vote vote = Vote.builder()
					.associated(associated.get())
					.session(session.get())
					.vote(VoteEnum.SIM) // Voto																												// "SIM"
					.build();
			votacaoService.save(vote);
		}

	}

}
