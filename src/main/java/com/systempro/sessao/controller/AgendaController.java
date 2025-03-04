package com.systempro.sessao.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.service.AgendaService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/agendas")
@Tag(name = "Agenda Controller", description = "Endpoints for managing agendas") // Definindo a tag do controlador
public class AgendaController {

    private final AgendaService service;
    private final ModelMapper mapper;

    public AgendaController(AgendaService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new agenda", 
        description = "This endpoint creates a new agenda based on the provided agenda data",
        tags = {"Agenda Controller"}  // Adicionando a tag específica para este método
    )
    public AgendaDTO create(@RequestBody @Valid AgendaDTO dto) {        
        Agenda entity = mapper.map(dto, Agenda.class);        
        entity = service.save(entity);        
        return mapper.map(entity, AgendaDTO.class);
    }
}
