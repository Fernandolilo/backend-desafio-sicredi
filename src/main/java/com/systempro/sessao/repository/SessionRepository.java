package com.systempro.sessao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

	List<Session> findByStatus(StatusEnum status);

}
