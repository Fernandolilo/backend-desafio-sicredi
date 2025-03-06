package com.systempro.sessao.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ConfigBens {
	
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void AgendamentoTarefaz() {
		System.out.println("AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO");
	}
	
}
