package com.systempro.sessao.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.dto.AssociatedDTO;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;
import com.systempro.sessao.service.AssociatedService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(AssociedController.class) // Especificando explicitamente o controller a ser testado
@AutoConfigureMockMvc
public class AssociedControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AssociatedService service;
	
	@MockBean
	private ModelMapper modelMapper;
	
	private static String API = "/associates";

	@Test
	@DisplayName("Save new associated")
	public void saveNewAssociated() throws Exception {
		
		AssociatedNewDTO dto = AssociatedNewDTO.builder().cpf("12312312311").nome("Fernando") // Correspondente ao que o controller espera
				.build();

		String json = new ObjectMapper().writeValueAsString(dto);
		
		Associated associated = Associated.builder().cpf(dto.getCpf()).nome(dto.getNome()).build();
		AssociatedDTO associatedDTO = AssociatedDTO.builder().cpf(dto.getCpf()).nome(dto.getNome()).build();
		
		BDDMockito.given(modelMapper.map(Mockito.any(AssociatedNewDTO.class), Mockito.eq(Associated.class))).willReturn(associated);
		BDDMockito.given(modelMapper.map(Mockito.any(Associated.class), Mockito.eq(AssociatedDTO.class))).willReturn(associatedDTO);
		
		BDDMockito.given(service.save(Mockito.any(Associated.class))).willReturn(associated);
		// Criar e executar a requisição Mock
		
	
	
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(json);

		mockMvc.perform(request)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("nome").value(associatedDTO.getNome()))
		.andExpect(jsonPath("cpf").value(associatedDTO.getCpf()));
		
	}
}
