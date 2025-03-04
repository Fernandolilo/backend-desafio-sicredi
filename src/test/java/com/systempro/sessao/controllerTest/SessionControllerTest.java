package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
import com.systempro.sessao.entity.dto.SessionNewDTO;
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
		
		// Criar DTO correto
		SessionNewDTO dto = SessionNewDTO.builder()
		        .agenda("criada") // Correspondente ao que o controller espera
		        .build();

		String json = new ObjectMapper().writeValueAsString(dto);

		// Criar objeto Agenda válido
		Agenda agenda = Agenda.builder()
		        .description("criada")
		        .build();

		// Mock do serviço de agenda
		BDDMockito.given(agendaService.findByDescripton("criada")).willReturn(Optional.of(agenda));

		// Criar objeto Sessão válido com ID
		Session session = Session.builder()
		        .id(UUID.randomUUID())  // Adicionando ID válido
		        .inicio(agora)
		        .staus(StatusEnum.ABERTO)
		        .agenda(agenda)
		        .build();

		// Mock do serviço de sessão
		BDDMockito.given(service.save(Mockito.any(Session.class))).willReturn(session);

		// Criar e executar a requisição Mock
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(SESSION)
		        .accept(MediaType.APPLICATION_JSON)
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(json);

		mockMvc.perform(request)
		        .andExpect(status().isCreated());
	}

}
