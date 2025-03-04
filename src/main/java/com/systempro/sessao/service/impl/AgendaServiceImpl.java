package com.systempro.sessao.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.service.AgendaService;

@Service
public class AgendaServiceImpl implements AgendaService {
	
	private final AgendaRepository repository;
	

	public AgendaServiceImpl(AgendaRepository repository) {
		this.repository = repository;
	}


	@Override
	public Agenda save(Agenda agenda) {
		return repository.save(agenda);
	}


	@Override
	public Optional<Agenda> findByDescripton(String string) {		
		return repository.findByDescription(string);
	}


	@Override
	public boolean existsByDescription(String description) {
		return repository.existsByDescription(description);
	}




}
