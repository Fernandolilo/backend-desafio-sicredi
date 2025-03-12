package com.systempro.sessao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.entity.dto.AgendaNewDTO;
import com.systempro.sessao.service.AgendaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agendas")
@Tag(name = "Agenda Controller", description = "Endpoints for managing agendas") // Definindo a tag do controlador
public class AgendaController {

	private final AgendaService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Create a new agenda", description = "This endpoint creates a new agenda based on the provided agenda data", tags = {
			"Agenda Controller" } // Adicionando a tag específica para este método
	)
	public AgendaDTO create(@RequestBody @Valid AgendaNewDTO dto) {		
		return service.save(dto);
	}
}
