package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.AgendaDTO;
import com.systempro.sessao.entity.dto.AgendaNewDTO;
import com.systempro.sessao.repository.AgendaRepository;
import com.systempro.sessao.service.AgendaService;
import com.systempro.sessao.service.impl.AgendaServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AgendaServiceTest {

    @MockBean
    private AgendaRepository repository;

    private AgendaService service;

    @MockBean
    private ModelMapper mapper;

    @BeforeEach
    public void setUp() {
        this.service = new AgendaServiceImpl(mapper, repository);
    }

    @Test
    @DisplayName("Save new Agenda")
    public void saveAgendaTest() {
        // Criando um UUID fixo para o teste
        UUID id = UUID.fromString("a1b2c3d4-e5f6-7890-ab12-cd34ef56abcd");

        // Criando um DTO de entrada
        AgendaNewDTO agendaDTO = AgendaNewDTO.builder().description("criada").build();

        // Criando a entidade Agenda correspondente
        Agenda agendaEntity = Agenda.builder().id(id).description(agendaDTO.getDescription()).build();

        // Criando um DTO de saída esperado
        AgendaDTO expectedDTO = AgendaDTO.builder().id(id).description("criada").build();

        // Simulando a conversão de DTO para entidade no ModelMapper
        Mockito.when(mapper.map(agendaDTO, Agenda.class)).thenReturn(agendaEntity);

        // Simulando a conversão de entidade para DTO no ModelMapper
        Mockito.when(mapper.map(agendaEntity, AgendaDTO.class)).thenReturn(expectedDTO);

        // Simulando o salvamento no repositório
        Mockito.when(repository.save(any(Agenda.class))).thenReturn(agendaEntity);

        // Execução
        AgendaDTO savedAgenda = service.save(agendaDTO);

        // Verificação
        assertThat(savedAgenda).isNotNull();
        assertThat(savedAgenda.getId()).isEqualTo(id);
        assertThat(savedAgenda.getDescription()).isEqualTo("criada");
    }
}
