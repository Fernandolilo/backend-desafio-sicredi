package com.systempro.sessao.entity.dto;

import java.util.UUID;

import com.systempro.sessao.entity.Associated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociatedNewDTO {
	
	private String cpf;
	private String nome;

}
