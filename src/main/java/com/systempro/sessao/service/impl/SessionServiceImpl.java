package com.systempro.sessao.service.impl;

import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.SessionService;

@Service
public class SessionServiceImpl implements SessionService{
	
	private final SessionRepository repositoy;
	
	

	public SessionServiceImpl(SessionRepository repositoy) {
		this.repositoy = repositoy;
	}



	@Override
	public Session save(Session session) {
		return repositoy.save(session);
	}

}
