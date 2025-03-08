package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.SessionService;
import com.systempro.sessao.service.impl.SessionServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class SessionSeviceTest {


	@MockBean
	SessionRepository repository;
	
	SessionService service;
	
	
	private LocalDateTime agora = LocalDateTime.now();
	private LocalDateTime fim = agora.plusMinutes(1);
	
	
	@BeforeEach
	public void setUp() {
		this.service = new SessionServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Save new Agenda")
	public void saveAgendaTest() {
				
	    Agenda agenda = Agenda.builder().description("criada").build();
		
		Session session = Session.builder().inicio(agora).agenda(agenda).build();
		
		Mockito.when(repository.save(session)).thenReturn(session);
		
		//execução
		Session sessionSave = service.save(session);
		
		//verificação
		
		//assertThat(sessionSave.getId()).isNotNull();
		assertThat(sessionSave.getAgenda().getDescription()).isEqualTo("criada");
		
	}
	
	
	@Test
    @DisplayName("Deve retornar uma lista de sessões quando o status for ABERTO")
    void findByStatus_ReturnsSessions_WhenStatusIsAberto() {
        // 🔹 Configuração do ambiente
        LocalDateTime agora = LocalDateTime.now();

        Agenda agenda = Agenda.builder()
                .description("Sessão de Teste")
                .build();

        List<Session> sessions = List.of(
                Session.builder()
                        .id(UUID.randomUUID())
                        .inicio(agora)
                        .status(StatusEnum.ABERTO)  // Certifique-se que o nome do campo está correto
                        .agenda(agenda)
                        .build()
        );

        // 🔹 Configuração do Mock
        BDDMockito.given(repository.findByStatus(StatusEnum.ABERTO))
                  .willReturn(sessions);

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
        BDDMockito.given(repository.findByStatus(StatusEnum.FECHADO))
                  .willReturn(Collections.emptyList());

        // 🔹 Execução do método
        List<Session> result = service.findByStatus(StatusEnum.FECHADO);

        // 🔹 Verificações
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // 🔹 Verifica se o método foi chamado corretamente
        Mockito.verify(repository, Mockito.times(1)).findByStatus(StatusEnum.FECHADO);
    }
	
}
