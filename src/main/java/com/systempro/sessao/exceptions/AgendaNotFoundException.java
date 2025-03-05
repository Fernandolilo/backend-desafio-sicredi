package com.systempro.sessao.exceptions;

public class AgendaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AgendaNotFoundException(String message) {
		super(message);
	}

}
