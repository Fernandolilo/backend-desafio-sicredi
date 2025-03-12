package com.systempro.sessao.service;

import java.util.Optional;
import java.util.UUID;

import com.systempro.sessao.entity.Associated;
import com.systempro.sessao.entity.dto.AssociatedDTO;
import com.systempro.sessao.entity.dto.AssociatedNewDTO;

public interface AssociatedService {
	
	AssociatedDTO save(AssociatedNewDTO any);
	
	Optional<Associated> findById(UUID id);

}
