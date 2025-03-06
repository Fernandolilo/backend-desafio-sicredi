package com.systempro.sessao.service.impl;

import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService{

	private final VoteRepository repository;

	
	public VoteServiceImpl(VoteRepository repository) {
		this.repository = repository;
	}


	@Override
	public Vote save(Vote vote) {
		// TODO Auto-generated method stub
		return repository.save(vote);
	}

	
}
