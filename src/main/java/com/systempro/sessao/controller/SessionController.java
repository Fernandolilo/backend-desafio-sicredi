package com.systempro.sessao.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.dto.SessionDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sessions")
public class SessionController {

	private final SessionService service;
	private final AgendaService agendaService;

	public SessionController(SessionService service, AgendaService agendaService) {
		this.service = service;
		this.agendaService =agendaService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody @Valid SessionDTO obj) {
		
		Agenda agenda = agendaService.findByDescipton(obj.getAgenda().getDescription()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não existe pauta para seguir com sessão"));
		
		Session entity = Session
				.builder()
				.inicio(LocalDateTime.now())
				.staus(StatusEnum.ABERTO)
				.agenda(agenda).build();
		
		entity = service.save(entity);
		
		return entity.getId();		
		
	}

}
