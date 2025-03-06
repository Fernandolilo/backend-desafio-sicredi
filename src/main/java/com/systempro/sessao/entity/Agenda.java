package com.systempro.sessao.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Agenda {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	@NotEmpty
	private String description;
	
	@JsonBackReference
	@OneToMany(mappedBy = "agenda", fetch = FetchType.LAZY)
	private List<Session> sessions;
}
