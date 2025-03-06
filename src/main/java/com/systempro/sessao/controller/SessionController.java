package com.systempro.sessao.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.dto.SessionNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.exceptions.AgendaNotFoundException;
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
		this.agendaService = agendaService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody @Valid SessionNewDTO obj) {
		Agenda agenda = agendaService.findByDescripton(obj.getAgenda())
				.orElseThrow(() -> new AgendaNotFoundException("Não existe pauta para seguir com sessão"));

		Session entity = Session.builder().inicio(LocalDateTime.now()).status(StatusEnum.ABERTO).agenda(agenda).build();

		// Como já validamos que a agenda existe, não é necessário chamar
		// `existsByDescription` novamente
		entity = service.save(entity);

		return entity.getId();
	}
	
	@GetMapping("/aberto")  
    public List<Session> findAllByStatus(@RequestParam StatusEnum status) {
        return service.findByStatus(status);
    }
}
