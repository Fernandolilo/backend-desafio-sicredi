package com.systempro.sessao.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;

public interface SessionService {
	Session save(Session any);

	public List<Session> findByStatus(StatusEnum status);

	Optional<Session> findById(UUID id_session);


}
