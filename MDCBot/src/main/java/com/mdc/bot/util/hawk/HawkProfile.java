package com.mdc.bot.util.hawk;

import java.util.HashMap;
import java.util.Map;

public class HawkProfile {

	private long uniqueId;
	private Map<HawkUserStatistic,String> userStatistics;
	private final static String nullStat = "```";
	
	/**
	 * Create a new profile for this user
	 * @param id The user id
	 */
	public HawkProfile(long id) {
		this.uniqueId = id;
		userStatistics = new HashMap<HawkUserStatistic,String>();
		for(HawkUserStatistic s : HawkUserStatistic.values()) {
			userStatistics.put(s, nullStat);
		}
	}
	
	public long getUserId() {
		return this.uniqueId;
	}
	
}
