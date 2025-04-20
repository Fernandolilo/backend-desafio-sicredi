package com.systempro.sessao.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.enuns.VoteEnum;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

	long countBySession_IdAndVote(UUID sessionId, VoteEnum vote);
	boolean existsByAssociatedIdAndSessionId(UUID assossiateId, UUID sessionId);

}
