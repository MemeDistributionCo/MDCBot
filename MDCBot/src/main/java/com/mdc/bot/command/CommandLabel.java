package com.mdc.bot.command;


/**
 * An enumerated type which is where all valid commands are placed.
 * @author xDestx
 *
 */
public class CommandLabel {
	
	private String label;
	private Class<?> commandClass;
	
	/**
	 * Command label constructor
	 * @param label The label
	 * @param commandClass The class to execute
	 */
	public CommandLabel(String label, Class<?> commandClass) {
		this.label = label;
		this.commandClass = commandClass;
	}
	
	/**
	 * Get the label for this command
	 * @return The label
	 */
	String getLabel() {
		return this.label;
	}
	
	/**
	 * Get the executing class for this command
	 * @return The class
	 */
	Class<?> getCommandClass() {
		return this.commandClass;
	}
	
}
