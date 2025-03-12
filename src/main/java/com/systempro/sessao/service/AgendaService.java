package com.systempro.sessao.service;

import java.util.Optional;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.entity.dto.AgendaNewDTO;


public interface AgendaService {
	AgendaDTO save(AgendaNewDTO agenda);
	
	Optional<Agenda> findByDescription(String string);
	
	boolean existsByDescription(String description);
}
