package com.systempro.sessao.repositoy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.repository.SessionRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class SessionRepositoryTests {
	
	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	SessionRepository repository;

	
	@Autowired
	AgendaRepository agendaRepository;
	
	@Test
	@DisplayName("Created new session")
	public void createdNewSession() {
		
	String descripition = "criada";
		
		Agenda agenda = Agenda.builder().description(descripition).build();
		entityManager.persist(agenda);
		entityManager.flush();
		
		boolean exists = agendaRepository.existsByDescription(descripition);
		Session session = Session.builder().agenda(agenda).inicio(null).status(StatusEnum.ABERTO).build();

		if(exists) {
			entityManager.persist(session);
			entityManager.flush();
			
		}
				
		assertThat(session.getAgenda().getDescription()).isEqualTo(agenda.getDescription());
		
	}
	

	@Test
	public void findByStatus_ReturnsSessions_WhenStatusIsAberto() {
	    // Criação de dados de teste
	    LocalDateTime agora = LocalDateTime.now();

	    Agenda agenda = Agenda.builder()
	            .description("Sessão de Teste")
	            .build();
	    entityManager.persist(agenda);
		entityManager.flush();

	    Session session1 = Session.builder()
	            .id(UUID.randomUUID())  // Usando um ID único para evitar conflito
	            .inicio(agora)
	            .status(StatusEnum.ABERTO)
	            .agenda(agenda)
	            .build();

	    Session session2 = Session.builder()
	            .id(UUID.randomUUID())  // Usando um ID único para evitar conflito
	            .inicio(agora.plusHours(1))
	            .status(StatusEnum.ABERTO)
	            .agenda(agenda)
	            .build();

	    Session session3 = Session.builder()
	            .id(UUID.randomUUID())  // Usando um ID único para evitar conflito
	            .inicio(agora.plusHours(2))
	            .status(StatusEnum.FECHADO)
	            .agenda(agenda)
	            .build();

	    // Persistindo as entidades no banco de dados
	    entityManager.merge(session1);  // Usando merge para garantir que a entidade seja gerenciada corretamente
	    entityManager.merge(session2);  // Usando merge para garantir que a entidade seja gerenciada corretamente
	    entityManager.merge(session3);  // Usando merge para garantir que a entidade seja gerenciada corretamente
	    entityManager.flush();  // Garante que os dados sejam persistidos

	    // Executar o método do repositório
	    List<Session> sessions = repository.findByStatus(StatusEnum.ABERTO);

	    // Verificações
	    assertNotNull(sessions);
	    assertEquals(2, sessions.size());  // Esperamos 2 sessões com status ABERTO
	    assertTrue(sessions.stream().allMatch(session -> session.getStatus().equals(StatusEnum.ABERTO)));
	}


}
