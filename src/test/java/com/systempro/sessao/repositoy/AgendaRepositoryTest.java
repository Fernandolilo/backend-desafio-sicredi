package com.systempro.sessao.repositoy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.booleanThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.repository.AgendaRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class AgendaRepositoryTest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	AgendaRepository repository;
	
	@Test 
	@DisplayName("Created new agenda")
	public void CreatedNewAgenda() {
		
		String descripition = "criada";
		
		Agenda agenda = Agenda.builder().description(descripition).build();
		entityManager.persist(agenda);
		entityManager.flush();
		
		boolean exists = repository.existsByDescription(descripition);
		
		assertThat(exists).isTrue();
	}
	
	@Test 
	@DisplayName("Agenda false when does not description")
	public void falseWhenDoesNotDescription() {
		
		String descripition = "criada";		
		
		boolean exists = repository.existsByDescription(descripition);
		
		assertThat(exists).isFalse();
	}
}
