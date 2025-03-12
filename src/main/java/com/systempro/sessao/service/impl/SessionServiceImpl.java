package com.systempro.sessao.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.SessionNewDTO;
import com.systempro.sessao.entity.dto.VoteDTO;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.exceptions.AgendaNotFoundException;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.kafka.KafkaProducer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

	private final SessionRepository repositoy;
	private final AgendaService agendaService;
	private final AssociatedService associatedService;
	private final ModelMapper mapper;
	private final KafkaProducer kafkaProducerService; // Mock do Kafka

	@Override
	public Session save(Session session) {
		return repositoy.save(session);
	}

	@Override
	public List<Session> findByStatus(StatusEnum status) {
		// TODO Auto-generated method stub
		return repositoy.findByStatus(status);
	}

	@Override
	public Optional<Session> findById(UUID id_session) {
		// TODO Auto-generated method stub
		return repositoy.findById(id_session);
	}

	@Override
	@Transactional
	public UUID saveTo(SessionNewDTO objDto) {
		Agenda agenda = agendaService.findByDescription(objDto.getAgenda())
				.orElseThrow(() -> new AgendaNotFoundException("Não existe pauta para seguir com sessão"));

		Session entity = Session.builder().inicio(LocalDateTime.now()).fim(LocalDateTime.now())
				.status(StatusEnum.ABERTO).agenda(agenda).build();

		entity = repositoy.save(entity);

		return entity.getId();
	}

	@Override
	public VoteDTO voted(VoteNewDTO obj) {
		Session vote = findById(obj.getId_session())
				.orElseThrow(() -> new AgendaNotFoundException("Não existe sessão"));

		Associated associated = associatedService.findById(obj.getId_associate())
				.orElseThrow(() -> new AgendaNotFoundException("Associado não encontrado"));

		Vote entity = Vote.builder().associated(associated).session(vote).vote(obj.getVote()).build();

		// Como já validamos que a agenda existe, não é necessário chamar
		// `existsByDescription` novamente
		// entity = votacaoService.save(entity);
		kafkaProducerService.sendVote(obj);
		System.out.println("Chamada para o producer Kafka");

		VoteDTO dto = mapper.map(entity, VoteDTO.class);
		return dto;
	}

	@Override
	public UUID saveToVote(VoteNewDTO obj) {
		Session vote = findById(obj.getId_session())
				.orElseThrow(() -> new AgendaNotFoundException("Não existe sessão"));

		Associated associated = associatedService.findById(obj.getId_associate())
				.orElseThrow(() -> new AgendaNotFoundException("Associado não encontrado"));

		Vote entity = Vote.builder().associated(associated).session(vote).vote(obj.getVote()).build();

		// Salvar a votação (se necessário)
		// entity = votacaoService.save(entity);

		kafkaProducerService.sendVote(obj);
		System.out.println("Chamada para o producer Kafka");

		return entity.getId();
	}

}
