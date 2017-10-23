package com.mdc.bot.util.exception;

public class TokenNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6187436825931119167L;

	private final String filePath;
	
	/**
	 * Accepts on argument for the file path and one for the message
	 * @param msg Error message
	 * @param filePath Token file path
	 */
	public TokenNotFoundException(String msg, String filePath) {
		super(msg);
		this.filePath = filePath;
	}
	
	public final String getTokenPath() {
		return this.filePath;
	}
}
