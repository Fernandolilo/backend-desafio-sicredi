package com.systempro.sessao.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.dto.AssociatedDTO;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;
import com.systempro.sessao.repository.AssociatedRepository;
import com.systempro.sessao.service.AssociatedService;

@Service
public class AssociatedServiceImpl implements AssociatedService {

	private final AssociatedRepository repository;
	private final ModelMapper mapper;

	public AssociatedServiceImpl(AssociatedRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public AssociatedDTO save(AssociatedNewDTO associated) {

		Associated entity = mapper.map(associated, Associated.class);

		entity = repository.save(entity);

		AssociatedDTO dto = mapper.map(associated, AssociatedDTO.class);

		// TODO Auto-generated method stub
		return dto;
	}

	@Override
	public Optional<Associated> findById(UUID id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
