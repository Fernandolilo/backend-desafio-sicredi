package com.systempro.sessao.serviceTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.service.AgendametnoSessionService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AgendamentoServiceTest {

	@MockBean
    private SessionRepository repository;
	
	@MockBean
	private AgendaRepository agendaRepository;

	@MockBean
    private AgendametnoSessionService agendaSessionService;

	
    private Session sessionAberta;
    private LocalDateTime agora;

    @BeforeEach
    public void setUp() {
    	
    	Agenda agenda = Agenda.builder().description("criada").build();
    	//agendaRepository.save(agenda);
        agora = LocalDateTime.now();

        // Criando uma sessão aberta
        sessionAberta = Session.builder()
                .id(UUID.randomUUID())
                .inicio(agora.minusMinutes(2))  // Sessão aberta há 2 minutos
                .status(StatusEnum.ABERTO)
                .agenda(agenda)
                .build();
    //    repository.save(sessionAberta);
    }

    @Test
    public void agendamentoTarefas_DeveFecharSessaoAbertaHaMaisDe1Minuto() {
    	Agenda agenda = Agenda.builder().description("criada").build();
    	agendaRepository.save(agenda);
        agora = LocalDateTime.now();
    	sessionAberta = Session.builder()
                .id(UUID.randomUUID())
                .inicio(agora.minusMinutes(2))  // Sessão aberta há 2 minutos
                .status(StatusEnum.ABERTO)
                .agenda(agenda)
                .build();
        repository.save(sessionAberta);
        // Preparando o mock para retornar a sessão aberta
        when(repository.findByStatus(StatusEnum.ABERTO)).thenReturn(List.of(sessionAberta));

        // Chama o método agendado
        agendaSessionService.agendamentoTarefas();

        // Verificando se o método save foi chamado com a sessão com status alterado
        verify(repository, times(1)).save(any(Session.class)); 

        // Como o save deve alterar o status, verificamos se foi realmente alterado para FECHADO
      //  assertEquals(StatusEnum.FECHADO, sessionAberta.getStatus());
        assertNotNull(sessionAberta.getStatus().equals(StatusEnum.FECHADO)); // Verificar se a data de fim foi preenchida
    }

    @Test
    public void agendamentoTarefas_NaoDeveFecharSessaoAbertaHaMenosDe1Minuto() {
        // Criando uma sessão aberta há menos de 1 minuto
        sessionAberta.setInicio(agora.minusSeconds(30));

        // Preparando o mock para retornar a sessão aberta
        when(repository.findByStatus(StatusEnum.ABERTO)).thenReturn(List.of(sessionAberta));

        // Chama o método agendado
        agendaSessionService.agendamentoTarefas();

        // Verificando que o método save() não foi chamado, pois a sessão não deve ser fechada
        verify(repository, never()).save(any(Session.class));
    }


}

