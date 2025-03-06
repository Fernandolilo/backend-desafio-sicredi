package com.systempro.sessao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempro.sessao.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

}
