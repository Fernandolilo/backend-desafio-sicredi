package com.systempro.sessao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempro.sessao.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

}
