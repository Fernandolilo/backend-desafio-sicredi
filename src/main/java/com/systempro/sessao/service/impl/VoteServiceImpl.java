package com.systempro.sessao.service.impl;

import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.repository.VoteRepository;
import com.systempro.sessao.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository repository;

    public VoteServiceImpl(VoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vote save(Vote vote) {
    	 if (vote == null || vote.getSession() == null) {
    	        throw new IllegalArgumentException("Vote or Session cannot be null");
    	    }
        return repository.save(vote);
    }
}
