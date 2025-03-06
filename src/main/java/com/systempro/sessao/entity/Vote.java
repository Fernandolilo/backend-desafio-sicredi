package com.systempro.sessao.entity;

import java.util.UUID;

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
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;	
	private VoteEnum vote;
	
	
}
