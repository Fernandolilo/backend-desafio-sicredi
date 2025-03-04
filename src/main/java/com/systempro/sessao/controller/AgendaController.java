package com.systempro.sessao.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.dto.AgendaDTO;

@RestController
@RequestMapping("/agendas")
public class AgendaController {

	
	@PostMapping
	public AgendaDTO create() {
		return null;	
		
	}
}
