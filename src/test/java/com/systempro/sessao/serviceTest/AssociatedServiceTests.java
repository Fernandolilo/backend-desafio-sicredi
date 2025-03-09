package com.systempro.sessao.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;
import com.systempro.sessao.repository.AssociatedRepository;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AssociatedServiceTests {

	@MockBean
	private AssociatedRepository repository;

	@MockBean
	private ModelMapper modelMapper;

	private Associated associated;

	@BeforeEach
	public void setup() {
		AssociatedNewDTO associatedNewDTO = AssociatedNewDTO.builder().cpf("12312312311").nome("Fernando").build();

		associated = Associated.builder().cpf(associatedNewDTO.getCpf()).nome(associatedNewDTO.getNome()).build(); // Criando manualmente um objeto associado válido
		

		Mockito.when(modelMapper.map(associatedNewDTO, Associated.class)).thenReturn(associated);

		repository.save(associated);
	}

	@Test
	public void saveNewAssociated() {
		
		
		
		verify(repository, times(1)).save(associated);
		
	}
}
