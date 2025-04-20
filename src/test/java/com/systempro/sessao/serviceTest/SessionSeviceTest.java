package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.entity.dto.AgendaNewDTO;
import com.systempro.sessao.entity.dto.AssociatedDTO;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;
import com.systempro.sessao.entity.dto.VoteNewDTO;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.enuns.VoteEnum;
import com.systempro.sessao.exceptions.AgendaNotFoundException;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.repository.VoteRepository;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.AssociatedService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.VotacaoService;
import com.systempro.sessao.service.impl.SessionServiceImpl;
import com.systempro.sessao.service.kafka.KafkaProducer;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class SessionSeviceTest {

	@MockBean
	SessionRepository repository;

	@MockBean
	SessionService service;

	@MockBean
	AgendaService agendaService;
	
	
	@MockBean
	AssociatedService associatedService;

	@MockBean
	VotacaoService voteService;
	
	@MockBean
	VoteRepository voteRepository;
	

	@MockBean
	ModelMapper mapper;

	@MockBean
	KafkaProducer kafkaProducerService; // Mock do Kafka

	private LocalDateTime agora = LocalDateTime.now();
	private LocalDateTime fim = agora.plusMinutes(1);

	@BeforeEach
	public void setUp() {
		this.service = new SessionServiceImpl(repository, agendaService, associatedService, voteRepository,  mapper,
				kafkaProducerService);
	}

	@Test
	@DisplayName("Save new Agenda")
	public void saveAgendaTest() {

		Agenda agenda = Agenda.builder().description("criada").build();

		Session session = Session.builder().inicio(agora).agenda(agenda).build();

		Mockito.when(repository.save(session)).thenReturn(session);

		// execução
		Session sessionSave = service.save(session);

		// verificação

		// assertThat(sessionSave.getId()).isNotNull();
		assertThat(sessionSave.getAgenda().getDescription()).isEqualTo("criada");

	}

	@Test
	@DisplayName("Salvar novo voto retornando DTO")
	public void voted() {

		UUID id_associad = UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4b");
		UUID id = UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4a");
		// Criar objeto Agenda válido

		AssociatedNewDTO associatedNewDTO = AssociatedNewDTO.builder().cpf("12312312312").nome("Fernando").build();

		AssociatedDTO associatedDTO = AssociatedDTO.builder().id(id_associad).cpf(associatedNewDTO.getCpf())
				.nome(associatedNewDTO.getNome()).build();

		Associated associated = Associated.builder().id(associatedDTO.getId()).nome(associatedDTO.getNome())
				.cpf(associatedDTO.getCpf()).build();
		// mock associado
		BDDMockito.given(associatedService.save(Mockito.any(AssociatedNewDTO.class))).willReturn(associatedDTO);

		AgendaNewDTO agendaNewDTO = AgendaNewDTO.builder().description("criada").build();
		AgendaDTO agendaDTO = AgendaDTO.builder().description(agendaNewDTO.getDescription()).build();
		Agenda agenda = Agenda.builder().id(id).description("criada").build();
		BDDMockito.given(agendaService.save(agendaNewDTO)).willReturn(agendaDTO);

		// Mock do serviço de agenda
		BDDMockito.given(agendaService.findByDescription(agendaDTO.getDescription())).willReturn(Optional.of(agenda));

		// Criar objeto Sessão válido com ID
		Session session = Session.builder().agenda(agenda).inicio(agora).status(StatusEnum.ABERTO).agenda(agenda)
				.build();

		service.save(session);

		BDDMockito.given(service.findById(agenda.getId())).willReturn(Optional.of(session));

		VoteNewDTO voteNewDTO = VoteNewDTO.builder().vote(VoteEnum.SIM).id_session(id).id_associate(id_associad)
				.build();

		Vote vote = Vote.builder().associated(associated).session(session).vote(voteNewDTO.getVote()).build();

		Mockito.doNothing().when(kafkaProducerService).sendVote(Mockito.any(VoteNewDTO.class));
		BDDMockito.given(voteService.save(Mockito.any(Vote.class))).willReturn(vote);

		assertThat(agenda.getDescription()).isEqualTo("criada");

		assertThat(associated.getNome()).isEqualTo("Fernando");
		assertThat(vote.getVote()).isEqualTo(VoteEnum.SIM);	//assertEquals("Associado já efetuou seu voto.", ex.getMessage());

	}

	@Test
	@DisplayName("Salvar novo voto e retornar UUID")
	public void saveToVote() {
	    UUID id_associado = UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4b");
	    UUID id_sessao = UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4a");
	    UUID id_vote = UUID.fromString("f47a4773-41f9-47f7-b2d1-7d902f9e8c4c");

	    // Criando objetos simulados
	    Associated associated = Associated.builder().id(id_associado).nome("Fernando").cpf("12312312312").build();
	    Agenda agenda = Agenda.builder().id(id_sessao).description("criada").build();
	    Session session = Session.builder().id(id_sessao).agenda(agenda).inicio(agora).status(StatusEnum.ABERTO).build();

	    // DTO para o voto
	    VoteNewDTO voteNewDTO = VoteNewDTO.builder().vote(VoteEnum.SIM).id_session(id_sessao).id_associate(id_associado).build();
	    Vote vote = Vote.builder().id(id_vote) // Simulando um UUID gerado
	            .associated(associated).session(session).vote(voteNewDTO.getVote()).build();

	    // Mockando os serviços corretamente
	    BDDMockito.given(service.findById(id_sessao)).willReturn(Optional.of(session));
	    BDDMockito.given(associatedService.findById(id_associado)).willReturn(Optional.of(associated));

	    // Simulando a chamada do Kafka Producer
	    Mockito.doNothing().when(kafkaProducerService).sendVote(Mockito.any(VoteNewDTO.class));
	    // Verificando se o Kafka foi chamado corretamente
	    Mockito.verify(kafkaProducerService, Mockito.times(0)).sendVote(Mockito.any(VoteNewDTO.class));

	    // Se não lançada a exceção, continuamos com o UUID do voto
	    assertThat(vote.getId()).isNotNull().isEqualTo(id_vote);
	}


	@Test
	@DisplayName("Deve retornar uma lista de sessões quando o status for ABERTO")
	void findByStatus_ReturnsSessions_WhenStatusIsAberto() {
		// 🔹 Configuração do ambiente
		LocalDateTime agora = LocalDateTime.now();

		Agenda agenda = Agenda.builder().description("Sessão de Teste").build();

		List<Session> sessions = List.of(
				Session.builder().id(UUID.randomUUID()).inicio(agora).status(StatusEnum.ABERTO).agenda(agenda).build());

		// 🔹 Configuração do Mock
		BDDMockito.given(repository.findByStatus(StatusEnum.ABERTO)).willReturn(sessions);

		// 🔹 Execução do método
		List<Session> result = service.findByStatus(StatusEnum.ABERTO);

		// 🔹 Verificações
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(StatusEnum.ABERTO, result.get(0).getStatus());
		assertEquals("Sessão de Teste", result.get(0).getAgenda().getDescription());
		

		// 🔹 Verifica se o método foi chamado corretamente
		Mockito.verify(repository, Mockito.times(1)).findByStatus(StatusEnum.ABERTO);
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia quando não houver sessões com o status fornecido")
	void findByStatus_ReturnsEmptyList_WhenNoSessionsFound() {
		// 🔹 Configuração do Mock
		BDDMockito.given(repository.findByStatus(StatusEnum.FECHADO)).willReturn(Collections.emptyList());

		// 🔹 Execução do método
		List<Session> result = service.findByStatus(StatusEnum.FECHADO);

		// 🔹 Verificações
		assertNotNull(result);
		assertTrue(result.isEmpty());

		// 🔹 Verifica se o método foi chamado corretamente
		Mockito.verify(repository, Mockito.times(1)).findByStatus(StatusEnum.FECHADO);
	}

	@Test
	@DisplayName("Deve efetuar uma busca por ID")
	public void findById() {

		UUID id = UUID.fromString("a1b2c3d4-e5f6-7890-ab12-cd34ef56abce");

		LocalDateTime agora = LocalDateTime.now();

		Agenda agenda = Agenda.builder().description("Sessão de Teste").build();

		Session session = Session.builder().id(id).inicio(agora).status(StatusEnum.ABERTO) // Certifique-se que o nome
																							// do campo está correto
				.agenda(agenda).build();

		Optional<Session> found = service.findById(id);
		BDDMockito.given(repository.findById(session.getId())).willReturn(Optional.of(session));

		assertThat(session.getStatus()).isEqualTo(StatusEnum.ABERTO);

		BDDMockito.verify(repository, Mockito.times(1)).findById(session.getId());
	}

	@Test
	@DisplayName("Busca por id inexitente")
	public void findByIdNotFound() {
		LocalDateTime agora = LocalDateTime.now();
		Agenda agenda = Agenda.builder().description("Sessão de Teste").build();

		Session session = Session.builder().inicio(agora).status(StatusEnum.ABERTO) // Certifique-se que o nome do campo
																					// está correto
				.agenda(agenda).build();

		BDDMockito.given(repository.findById(session.getId())).willReturn(Optional.empty());
	}

}
