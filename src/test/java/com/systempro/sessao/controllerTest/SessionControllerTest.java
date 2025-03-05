package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
import com.systempro.sessao.controller.SessionController;
import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.entity.dto.SessionDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.SessionService;



@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(SessionController.class) // Especificando explicitamente o controller a ser testado
@AutoConfigureMockMvc
public class SessionControllerTest {

	private static String SESSION = "/sessions";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AgendaService agendaService;

	@MockBean
	private SessionService service;
	

	@Test
	@DisplayName("Sessions new")
	public void createSessions() throws Exception {
	    
	    LocalDateTime agora = LocalDateTime.now();
	    LocalDateTime fim = agora.plusMinutes(1);

	    // Criar um objeto AgendaDTO válido
	    AgendaDTO agendaDTO = AgendaDTO.builder()
	            .description("criada")
	            .build();

	    // Criar o DTO da Sessão com uma agenda válida
	    SessionDTO dto = SessionDTO.builder()
	            .inicio(agora)
	            .fim(fim)
	            .status(StatusEnum.ABERTO)
	            .agenda(agendaDTO) // Adicionando a agenda ao DTO
	            .build();

	    String json = new ObjectMapper().writeValueAsString(dto);

	    // Criar um objeto Agenda válido
	    Agenda agenda = Agenda.builder()
	            .description("criada")
	            .build();

	    // Mock do serviço de agenda
	    BDDMockito.given(agendaService.save(Mockito.any(Agenda.class))).willReturn(agenda);
	    BDDMockito.given(agendaService.getByDescipton("criada")).willReturn(Optional.of(agenda));

	    // Criar um objeto Sessão válido
	    Session session = Session.builder()
	            .inicio(agora)
	            .fim(fim)
	            .agenda(agenda)
	            .staus(StatusEnum.ABERTO)
	            .build();

	    // Mock do serviço de sessão
	    BDDMockito.given(service.save(Mockito.any(Session.class))).willReturn(session);

	    // Criar e executar a requisição Mock
	    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(SESSION)
	            .accept(MediaType.APPLICATION_JSON)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json);

	    mockMvc.perform(request)
	            .andExpect(status().isCreated())
	          ;
	}

}
