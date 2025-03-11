package com.systempro.sessao.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.SessionNewDTO;
import com.systempro.sessao.entity.dto.VoteDTO;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.exceptions.AgendaNotFoundException;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;
import com.systempro.sessao.service.kafka.KafkaProducer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sessions")
public class SessionController {

	private final KafkaProducer kafkaProducerService; // Mock do Kafka
	private final SessionService service;
	private final AgendaService agendaService;
	private final VotacaoService votacaoService;
	private final AssociatedService associatedService;
	private final ModelMapper mapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody @Valid SessionNewDTO obj) {
		Agenda agenda = agendaService.findByDescription(obj.getAgenda())
				.orElseThrow(() -> new AgendaNotFoundException("Não existe pauta para seguir com sessão"));
		Session entity = Session.builder().inicio(LocalDateTime.now()).fim(LocalDateTime.now()).status(StatusEnum.ABERTO).agenda(agenda).build();

		// Como já validamos que a agenda existe, não é necessário chamar
		// `existsByDescription` novamente

		entity = service.save(entity);

		return entity.getId();
	}

	@GetMapping("/aberto")
	public List<Session> findAllByStatus(@RequestParam StatusEnum status) {
		return service.findByStatus(status);
	}

	@PostMapping("/vote")
	@ResponseStatus(HttpStatus.CREATED)
	public UUID vote(@RequestBody @Valid VoteNewDTO obj) {
		Session vote = service.findById(obj.getId_session())
				.orElseThrow(() -> new AgendaNotFoundException("Não existe sessão"));

		Associated associated = associatedService.findById(obj.getId_associate())
				.orElseThrow(() -> new AgendaNotFoundException("Associado não encontrado"));

		Vote entity = Vote.builder().associated(associated).session(vote).vote(obj.getVote()).build();

		// Salvar a votação (se necessário)
		// entity = votacaoService.save(entity);

		kafkaProducerService.sendVote(obj);
		System.out.println("Chamada para o producer Kafka");

		return obj.getId_session();
	}

	@PostMapping("/votedto")
	@ResponseStatus(HttpStatus.CREATED)
	public VoteDTO voted(@RequestBody @Valid VoteNewDTO obj) {
		/*
		 * if (obj.getId_session() == null) { throw new
		 * IllegalArgumentException("ID da sessão não pode ser nulo"); }
		 */
		Session vote = service.findById(obj.getId_session())
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

}
