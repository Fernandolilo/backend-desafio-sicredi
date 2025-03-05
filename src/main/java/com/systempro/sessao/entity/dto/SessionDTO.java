package com.systempro.sessao.entity.dto;

import java.time.LocalDateTime;

import com.systempro.sessao.enuns.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

	private LocalDateTime inicio;
	private LocalDateTime fim;
	private AgendaDTO agenda;
	private StatusEnum staus;
	

}
