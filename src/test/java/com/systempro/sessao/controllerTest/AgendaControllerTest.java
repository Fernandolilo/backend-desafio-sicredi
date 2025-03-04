package com.systempro.sessao.controllerTest;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class AgendaControllerTest {

	static String API = "/agendas";

	final MockMvc mock;

	public AgendaControllerTest(MockMvc mock) {
		this.mock = mock;
	}

	@DisplayName("Creanted new agenda")
	@Test
	public void createNewAgendaTest() throws Exception {

		String json = new ObjectMapper().writeValueAsString(null);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(json);
		mock.perform(request).andExpect(status().isCreated()).andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("agenda").value("criada"));

	}

}
