package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.impl.SessionServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class SessionSeviceTest {


	@MockBean
	SessionRepository repository;
	
	SessionService service;
	
	
	@BeforeEach
	public void setUp() {
		this.service = new SessionServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Save new Agenda")
	public void saveAgendaTest() {
		LocalDateTime agora = LocalDateTime.now();
	    LocalDateTime fim = agora.plusMinutes(1);
		
	    Agenda agenda = Agenda.builder().description("criada").build();
		
		Session session = Session.builder().inicio(agora).agenda(agenda).build();
		
		Mockito.when(repository.save(session)).thenReturn(session);
		
		//execução
		Session sessionSave = service.save(session);
		
		//verificação
		
		//assertThat(sessionSave.getId()).isNotNull();
		assertThat(sessionSave.getAgenda().getDescription()).isEqualTo("criada");
		
	}
	
}
