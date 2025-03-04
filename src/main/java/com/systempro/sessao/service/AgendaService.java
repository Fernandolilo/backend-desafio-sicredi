package com.systempro.sessao.service;

import java.util.Optional;

import com.systempro.sessao.entity.Agenda;


public interface AgendaService {
	Agenda save(Agenda agenda);
	Optional<Agenda> findByDescripton(String string);
	
	boolean existsByDescription(String description);
}
