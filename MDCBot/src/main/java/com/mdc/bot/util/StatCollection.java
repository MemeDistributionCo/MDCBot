package com.mdc.bot.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to store stats for whatever needed.
 * @author xDest
 *
 */
public class StatCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3976170737603350251L;

	private final String name;
	
	private Map<String,Integer> stats;
	
	
	/**
	 * Stat Collection, with a name
	 * @param statCollectionName The name used for this group of statistics
	 */
	public StatCollection(String statCollectionName) {
		this.name = statCollectionName;
		this.stats = new HashMap<String,Integer>();
	}
	
	public Map<String,Integer> getStatMap() {
		return this.stats;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setStat(String newStat,Integer statVal) {
		this.stats.put(newStat, statVal);
	}
}
