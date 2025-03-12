package com.systempro.sessao.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.dto.SessionNewDTO;
import com.systempro.sessao.entity.dto.VoteDTO;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;

public interface SessionService {
	Session save(Session any);
	
	UUID saveTo(SessionNewDTO obj);

	public List<Session> findByStatus(StatusEnum status);

	Optional<Session> findById(UUID id_session);
	
	VoteDTO voted(VoteNewDTO obj);
	
	UUID saveToVote(VoteNewDTO obj);


}
