package com.systempro.sessao.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AgendametnoSessionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AgendamentoSessionServiceImpl implements AgendametnoSessionService {

	private final SessionRepository repository;	

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

	
	
}
