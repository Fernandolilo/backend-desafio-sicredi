package com.systempro.sessao.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.dto.SessionNewDTO;
import com.systempro.sessao.entity.dto.VoteCountsDTO;
import com.systempro.sessao.entity.dto.VoteDTO;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sessions")
public class SessionController {

	private final SessionService service;
	private final VotacaoService votacaoService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UUID create(@RequestBody @Valid SessionNewDTO obj) {
		return service.saveTo(obj);
	}

	@GetMapping("/aberto")
	public List<Session> findAllByStatus(@RequestParam StatusEnum status) {
		return service.findByStatus(status);
	}

	@PostMapping("/vote")
	@ResponseStatus(HttpStatus.CREATED)
	public UUID vote(@RequestBody @Valid VoteNewDTO obj) {
		return service.saveToVote(obj);
	}

	@PostMapping("/votedto")
	@ResponseStatus(HttpStatus.CREATED)
	public VoteDTO voted(@RequestBody @Valid VoteNewDTO obj) {
		return service.voted(obj);
	}

	@GetMapping("/count/{sessionId}")
	public ResponseEntity<VoteCountsDTO> countVotes(@PathVariable UUID sessionId) {
		return ResponseEntity.ok(votacaoService.countVotesBySession(sessionId));
	}

}
