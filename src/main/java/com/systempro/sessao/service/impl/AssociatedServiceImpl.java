package com.systempro.sessao.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.repository.AssociatedRepository;
import com.systempro.sessao.service.AssociatedService;

@Service
public class AssociatedServiceImpl implements AssociatedService {

	private final AssociatedRepository repository;

	public AssociatedServiceImpl(AssociatedRepository repository) {
		this.repository = repository;
	}

	@Override
	public Associated save(Associated associated) {
		// TODO Auto-generated method stub
		return repository.save(associated);
	}

	@Override
	public Optional<Associated> findById(UUID id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
