package com.systempro.sessao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempro.sessao.entity.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, UUID>{

}
