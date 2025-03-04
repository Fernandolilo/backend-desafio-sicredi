package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.Agenda;
import com.systempro.sessao.entity.dto.AgendaDTO;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class AgendaControllerTest {

	static String API = "/agendas";

	@Autowired
	private  MockMvc mock;

	

	@DisplayName("Creanted new agenda")
	@Test
	public void createNewAgendaTest() throws Exception {
		
		AgendaDTO dto = AgendaDTO.builder().decription("criada").build();
		Agenda agenda = Agenda.builder().decription("criada").build();
		
		BDDMockito.given(service.save(Mockito.any(Agenda.class))).willReturn(agenda);
		
		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mock.perform(request)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(dto.getId()))
				.andExpect(jsonPath("decription").value(dto.getDecription()));

	}

}
