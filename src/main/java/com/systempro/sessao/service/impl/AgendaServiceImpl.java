package com.systempro.sessao.service.impl;

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
		if(!agenda.getDecription().isEmpty()){
			return repository.save(agenda);
		}
		return null;
	}

}
