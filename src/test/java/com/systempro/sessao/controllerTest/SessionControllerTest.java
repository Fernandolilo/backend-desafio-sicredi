package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.controller.SessionController;
import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.SessionNewDTO;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.enuns.VoteEnum;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;

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
	private AssociatedService associatedService;

	@MockBean
	private SessionService service;

	@MockBean
	private VotacaoService votacaoService;

	@MockBean
	private ModelMapper modelMapper;
	
	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;

	@Test
	@DisplayName("Sessions new")
	public void createSessions() throws Exception {

		LocalDateTime agora = LocalDateTime.now();

		// Criar DTO correto
		SessionNewDTO dto = SessionNewDTO.builder().agenda("criada") // Correspondente ao que o controller espera
				.build();

		String json = new ObjectMapper().writeValueAsString(dto);

		// Criar objeto Agenda válido
		Agenda agenda = Agenda.builder().description("criada").build();

		// Mock do serviço de agenda
		BDDMockito.given(agendaService.findByDescription("criada")).willReturn(Optional.of(agenda));

		// Criar objeto Sessão válido com ID
		Session session = Session.builder().id(UUID.randomUUID()) // Adicionando ID válido
				.inicio(agora).status(StatusEnum.ABERTO).agenda(agenda).build();

		// Mock do serviço de sessão
		BDDMockito.given(service.save(Mockito.any(Session.class))).willReturn(session);

		// Criar e executar a requisição Mock
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(SESSION).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Find all sessions by status")
	public void findAllSessionsByStatus() throws Exception {
		LocalDateTime agora = LocalDateTime.now();

		// Criar objeto Agenda
		Agenda agenda = Agenda.builder().description("criada").build();

		// Criar lista de sessões
		List<Session> sessions = List.of(Session.builder().id(UUID.randomUUID()).inicio(agora).status(StatusEnum.ABERTO) 
				.agenda(agenda).build());

		// Mock correto para um método que retorna List<Session>
		BDDMockito.given(service.findByStatus(StatusEnum.ABERTO)).willReturn(sessions);

		// Executar requisição Mock com parâmetro de status
		mockMvc.perform(MockMvcRequestBuilders.get(SESSION.concat("/aberto")).param("status", "ABERTO") // Passando
																										// status como
																										// parâmetro
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(sessions.size())) // Verifica tamanho da lista
				.andExpect(jsonPath("$[0].id").exists()) // Confirma que há um ID na sessão retornada
				.andExpect(jsonPath("$[0].status").value("ABERTO")) // Confirma que o status retornado é "ABERTO"
				.andExpect(jsonPath("$[0].agenda.description").value("criada")); // Confirma descrição da agenda
	}

	@Test
	@DisplayName("vote agenda")
	public void voteAgenda() throws Exception {

		LocalDateTime agora = LocalDateTime.now();

		// Criar objeto Agenda válido
		Agenda agenda = Agenda.builder().description("criada").build();

		// Mock do serviço de agenda
		BDDMockito.given(agendaService.findByDescription("criada")).willReturn(Optional.of(agenda));

		UUID id = UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4a"); // Adicionando ID válido

		// Criar objeto Sessão válido com ID
		Session session = Session.builder().inicio(agora).status(StatusEnum.ABERTO).agenda(agenda).build();

		// Mock do serviço de sessão
		BDDMockito.given(service.save(Mockito.any(Session.class))).willReturn(session);

		VoteNewDTO voteNewDTO = VoteNewDTO.builder().vote(VoteEnum.SIM).id_session(id) // Certifique-se de passar um ID
																						// válido
				.build();
		BDDMockito.given(service.findById(id)).willReturn(Optional.of(session));

		Vote vote = Vote.builder().session(session).vote(voteNewDTO.getVote()).build();

		BDDMockito.given(votacaoService.save(Mockito.any(Vote.class))).willReturn(vote);

		String json = new ObjectMapper().writeValueAsString(voteNewDTO);

		// Criar e executar a requisição Mock
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(SESSION.concat("/vote"))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request).andExpect(status().isCreated());
		// .andExpect(jsonPath("vote").value("SIM"));
	} 

	
	
	@Test
	@DisplayName("vote agenda dto")
	public void voteAgendadto() throws Exception {
		LocalDateTime agora = LocalDateTime.now();

		// Criar DTO correto
		VoteNewDTO dto = VoteNewDTO.builder().vote(VoteEnum.SIM) // Define o voto
				.id_session(UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4a")) // ID da sessão
				.build();

		// Criar objeto Agenda válido
		Agenda agenda = Agenda.builder().description("criada").build();

		// Mock do serviço de agenda
		BDDMockito.given(agendaService.findByDescription("criada")).willReturn(Optional.of(agenda));

		// Criar objeto Sessão válido com ID
		Session session = Session.builder().id(UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4a")).inicio(agora)
				.status(StatusEnum.ABERTO).agenda(agenda).build();

		// Mock do serviço de sessão
		BDDMockito.given(service.findById(UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4a")))
				.willReturn(Optional.of(session));

		// Criar o voto
		Vote vote = Vote.builder().session(session).vote(VoteEnum.SIM) // Voto "SIM"
				.build();

		// Mock do serviço de voto
		BDDMockito.given(votacaoService.save(Mockito.any(Vote.class))).willReturn(vote);

		// Converter o DTO para JSON
		String json = new ObjectMapper().writeValueAsString(dto);

		// Criar e executar a requisição Mock
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/sessions/votedto")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json);

		// Executar o teste
		mockMvc.perform(request).andExpect(status()
				.isCreated()) // Espera status HTTP 201
		; // Verifica se o voto foi "SIM"
	}

}
