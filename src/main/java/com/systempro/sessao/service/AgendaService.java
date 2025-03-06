package com.systempro.sessao.service;

import java.util.Optional;

import com.systempro.sessao.entity.Agenda;


public interface AgendaService {
	Agenda save(Agenda agenda);
	Optional<Agenda> findByDescription(String string);
	
	boolean existsByDescription(String description);
}
