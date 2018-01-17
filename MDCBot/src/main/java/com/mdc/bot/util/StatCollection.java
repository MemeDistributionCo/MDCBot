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
	
	/**
	 * Get the map of individual stats
	 * @return A map of integers mapped by strings
	 */
	public Map<String,Integer> getStatMap() {
		return this.stats;
	}
	
	/**
	 * The collection name
	 * @return A string of the collection name 
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Set a specific statistic
	 * @param newStat New stat key
	 * @param statVal New stat value
	 */
	public void setStat(String newStat,Integer statVal) {
		this.stats.put(newStat, statVal);
	}
}
