package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.controller.AgendaController;
import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.service.AgendaService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(AgendaController.class) // Especificando explicitamente o controller a ser testado
@AutoConfigureMockMvc
public class AgendaControllerTest {

	static String API = "/agendas";

	@Autowired
	private MockMvc mock;

	@MockBean
	private AgendaService service;

	@MockBean
	private ModelMapper modelMapper;

	@DisplayName("Created new agenda")
	@Test
	public void createNewAgendaTest() throws Exception {
		
		AgendaDTO dto = AgendaDTO.builder()
				.id(UUID.fromString("a1b2c3d4-e5f6-7890-ab12-cd34ef56abcd"))
				.decription("criada")
				.build();
		
		Agenda agenda = Agenda.builder()
				.decription("criada")
				.build();
		
		// Configurando o comportamento do ModelMapper
		BDDMockito.given(modelMapper.map(Mockito.any(AgendaDTO.class), Mockito.eq(Agenda.class))).willReturn(agenda);
		BDDMockito.given(modelMapper.map(Mockito.any(Agenda.class), Mockito.eq(AgendaDTO.class))).willReturn(dto);

		BDDMockito.given(service.save(Mockito.any(Agenda.class))).willReturn(agenda);
		
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mock.perform(request)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value("a1b2c3d4-e5f6-7890-ab12-cd34ef56abcd"))
				.andExpect(jsonPath("decription").value(dto.getDecription()));
	}
}

