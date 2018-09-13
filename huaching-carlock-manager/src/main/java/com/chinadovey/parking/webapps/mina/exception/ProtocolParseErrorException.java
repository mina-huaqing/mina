package com.chinadovey.parking.webapps.mina.exception;

public class ProtocolParseErrorException extends Exception {

	public ProtocolParseErrorException(String message) {
		super(message);
	}

	public ProtocolParseErrorException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8435841056688165210L;

}
