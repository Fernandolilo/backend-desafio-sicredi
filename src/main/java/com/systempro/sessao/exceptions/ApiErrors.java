package com.systempro.sessao.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;

public class ApiErrors {

	private List<String> errors;

	public ApiErrors(BindingResult bindingResult) {
		this.errors = new ArrayList<>();
		bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));

	}
	
	public ApiErrors(HttpMessageNotReadableException ex) {
		this.errors = Arrays.asList(ex.getMessage());
	}

	public ApiErrors(AgendaNotFoundException ex) {
		ex.getStackTrace();
		this.errors = Arrays.asList(ex.getMessage());
	}
	
	public ApiErrors(IllegalArgumentException ex) {
		this.errors = Arrays.asList(ex.getMessage());
	}

	
	

	public List<String> getErrors() {
		return errors;
	}
	
	
}
