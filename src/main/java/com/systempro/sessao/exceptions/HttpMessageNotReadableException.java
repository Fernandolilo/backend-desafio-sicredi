package com.systempro.sessao.exceptions;

public class HttpMessageNotReadableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public HttpMessageNotReadableException(String message) {
		super(message);
	}
}
