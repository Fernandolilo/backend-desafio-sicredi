package com.systempro.sessao.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.entity.dto.AgendaNewDTO;
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.service.AgendaService;

@Service
public class AgendaServiceImpl implements AgendaService {

	private final ModelMapper mapper;
	private final AgendaRepository repository;

	public AgendaServiceImpl(ModelMapper mapper, AgendaRepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}

	@Override
	public AgendaDTO save(AgendaNewDTO agenda) {
		Agenda entity = mapper.map(agenda, Agenda.class);
		entity = repository.save(entity);		
		AgendaDTO dto = mapper.map(entity, AgendaDTO.class);
		return dto;
	}

	@Override
	public boolean existsByDescription(String description) {
		return repository.existsByDescription(description);
	}

	@Override
	public Optional<Agenda> findByDescription(String string) {
		// TODO Auto-generated method stub
		return repository.findByDescription(string);
	}

}
