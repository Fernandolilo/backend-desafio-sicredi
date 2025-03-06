package com.systempro.sessao.entity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.systempro.sessao.enuns.StatusEnum;
import com.systempro.sessao.enuns.VoteEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VoteDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private VoteEnum vote;
	
	
}
