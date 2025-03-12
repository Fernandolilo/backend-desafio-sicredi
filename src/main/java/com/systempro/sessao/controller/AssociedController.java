package com.systempro.sessao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.dto.AssociatedDTO;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;
import com.systempro.sessao.service.AssociatedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/associates")
public class AssociedController {

	private final AssociatedService service;

	public AssociedController(AssociatedService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AssociatedDTO create(@RequestBody @Valid AssociatedNewDTO obj) {
		return service.save(obj);
	}

}
