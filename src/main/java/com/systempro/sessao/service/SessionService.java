package com.systempro.sessao.service;

import java.util.List;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;

public interface SessionService {
	Session save(Session any);

	public List<Session> findByStatus(StatusEnum status);


}
