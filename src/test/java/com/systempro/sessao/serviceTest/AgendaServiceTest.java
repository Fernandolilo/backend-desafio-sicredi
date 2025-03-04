package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.impl.AgendaServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AgendaServiceTest {

	@MockBean
	AgendaRepository repository;
	
	AgendaService service;
	
	@BeforeEach
	public void setUp() {
		this.service = new AgendaServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Save new Agenda")
	public void saveAgendaTest() {
		Agenda agenda = Agenda.builder().id(UUID.fromString("a1b2c3d4-e5f6-7890-ab12-cd34ef56abcd")).decription("criada").build();
		
		Mockito.when(repository.save(agenda)).thenReturn(Agenda.builder().id(UUID.fromString("a1b2c3d4-e5f6-7890-ab12-cd34ef56abcd")).decription("criada").build());
		
		//execução
		Agenda savedAgenda = service.save(agenda);
		
		//verificação
		
		assertThat(savedAgenda.getId()).isNotNull();
		assertThat(savedAgenda.getDecription()).isEqualTo("criada");
		
	}
}
