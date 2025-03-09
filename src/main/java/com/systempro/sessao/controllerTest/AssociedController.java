package com.systempro.sessao.controllerTest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.dto.AssociatedDTO;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;
import com.systempro.sessao.service.AssociatedService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/associates")
public class AssociedController {

	private final AssociatedService service;
	private final ModelMapper mapper;

	public AssociedController(AssociatedService service, ModelMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AssociatedDTO create(@RequestBody @Valid AssociatedNewDTO obj) {

		Associated associated = mapper.map(obj, Associated.class);

		associated = service.save(associated);

		AssociatedDTO dto = mapper.map(associated, AssociatedDTO.class);

		return dto;

	}

}
