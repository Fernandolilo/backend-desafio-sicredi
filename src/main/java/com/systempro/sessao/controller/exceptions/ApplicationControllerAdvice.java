package com.systempro.sessao.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.systempro.sessao.exceptions.ApiErrors;
import com.systempro.sessao.exceptions.AgendaNotFoundException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationsExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		return new ApiErrors(bindingResult);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return new ApiErrors(ex);
	}
	
	@ExceptionHandler(AgendaNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors responseStatusException(AgendaNotFoundException ex) {
		return new ApiErrors(ex);
	}

}
