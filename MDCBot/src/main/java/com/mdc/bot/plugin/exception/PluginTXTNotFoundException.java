package com.mdc.bot.plugin.exception;

public class PluginTXTNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5593529513801920918L;

	private final String fileName;
	
	public PluginTXTNotFoundException(String fileName) {
		super("Couldn't find plugin.txt in " + fileName);
		this.fileName = fileName;
	}

	
	public String getFileName() {
		return this.fileName;
	}
	
}
