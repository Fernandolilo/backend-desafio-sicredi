package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.Session;
import com.systempro.sessao.entity.Vote;
import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.enuns.VoteEnum;
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.repository.AssociatedRepository;
import com.systempro.sessao.repository.SessionRepository;
import com.systempro.sessao.repository.VoteRepository;
import com.systempro.sessao.service.AgendametnoSessionService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class VotacaoSeviceTest {
	
	@MockBean
	private VoteRepository repository;
	
	@MockBean
	private AssociatedRepository associatedRepository;
	
	@MockBean
    private SessionRepository SessionRepository;
	
	@MockBean
	private AgendaRepository agendaRepository;

	@MockBean
    private AgendametnoSessionService agendaSessionService;

	private Agenda agendaAberta;
    private Session sessionAberta;
    private Vote voteAberto;
    private Associated associatedVote;
    private LocalDateTime agora = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
    	
       agendaAberta = Agenda.builder()
    		   .description("criada")
    		   .build();
    	//agendaRepository.save(agenda);
        agora = LocalDateTime.now();

        // Criando uma sessão aberta
        sessionAberta = Session.builder()
                .id(UUID.randomUUID())
                .inicio(agora.minusMinutes(2))  // Sessão aberta há 2 minutos
                .status(StatusEnum.ABERTO)
                .agenda(agendaAberta)
                .build();
        SessionRepository.save(sessionAberta);
                
        associatedVote = Associated
        		.builder()
        		.cpf("12312312311")
        		.nome("Fernando")
        		.build();
        associatedRepository.save(associatedVote);
        
        
                
        voteAberto =Vote.builder().associated(associatedVote).session(sessionAberta).vote(VoteEnum.SIM).build();
        	repository.save(voteAberto);
    }

    @Test
    @DisplayName("Start Vote")
    public void voteNew() {
    	assertThat(voteAberto.getSession().getAgenda()).isEqualTo(agendaAberta);
    	assertThat(voteAberto.getAssociated()).isEqualTo(associatedVote);
    	assertThat(voteAberto.getSession()).isEqualTo(sessionAberta);
    	
        verify(repository, times(1)).save(any(Vote.class)); 
    }
}
