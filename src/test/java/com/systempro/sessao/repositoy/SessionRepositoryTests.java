package com.systempro.sessao.repositoy;

import static org.assertj.core.api.Assertions.assertThat;

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
		Session session = Session.builder().agenda(agenda).inicio(null).staus(StatusEnum.ABERTO).build();

		if(exists) {
			entityManager.persist(session);
			entityManager.flush();
			
		}
		
		
		assertThat(session.getAgenda().getDescription()).isEqualTo(agenda.getDescription());
		
		
	}

}
