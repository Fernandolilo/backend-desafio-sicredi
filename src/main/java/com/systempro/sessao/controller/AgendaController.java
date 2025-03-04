package com.systempro.sessao.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.dto.AgendaDTO;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AgendaDTO create() {		
		AgendaDTO age = new AgendaDTO();
		age.setId(UUID.fromString("a1b2c3d4-e5f6-7890-ab12-cd34ef56abcd"));

		age.setDecription("criada");		
		return age;
		
	}
}
