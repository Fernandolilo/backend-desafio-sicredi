package com.systempro.sessao.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;
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



	@Override
	public List<Session> findByStatus(StatusEnum status) {
		// TODO Auto-generated method stub
		return repositoy.findByStatus(status);
	}

}
