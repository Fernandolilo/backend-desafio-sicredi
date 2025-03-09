package com.systempro.sessao.service;

import java.util.Optional;
import java.util.UUID;

import com.systempro.sessao.entity.Associated;

public interface AssociatedService {
	
	Associated save(Associated any);
	
	Optional<Associated> findById(UUID id);

}
