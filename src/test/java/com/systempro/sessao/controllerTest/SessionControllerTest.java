package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.controller.SessionController;
import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.SessionDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.service.AgendaService;



@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(SessionController.class) // Especificando explicitamente o controller a ser testado
@AutoConfigureMockMvc
public class SessionControllerTest {

	private String SESSION = "/sessions";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AgendaService agendaService;

	@Test
	@DisplayName("Session new")
	public void createSession() throws Exception {

		LocalDateTime agora = LocalDateTime.now();
		LocalDateTime fim = agora.plusMinutes(1);

			
		SessionDTO dto = SessionDTO.builder().inicio(agora).fim(fim).staus(StatusEnum.ABERTO).build();
		

		String json = new ObjectMapper().writeValueAsString(dto);
	
		Agenda agenda = Agenda.builder().description("criada").build();
		
		BDDMockito.given(agendaService.getByDescipton("criada")).willReturn(Optional.of(agenda));
		
		Session session = Session.builder().inicio(agora).fim(fim).staus(StatusEnum.ABERTO).build();
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(SESSION)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request)
					.andExpect(status().isCreated())
					.andExpect(content().string("1"));
				;

		
	}

}
