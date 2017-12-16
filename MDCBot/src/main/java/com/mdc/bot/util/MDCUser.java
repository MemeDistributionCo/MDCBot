package com.mdc.bot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The intention of this class is to store all custom information about the user. Duel stats, updanks, and whatever future stats we add.
 * @author xDest
 *
 */
public class MDCUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7878121990963606360L;

	private final long userId;
	
	private Set<StatCollection> stats;
	
	private MDCUser(long userId) {
		this.userId = userId;
		this.stats = new HashSet<StatCollection>();
	}
	
	/**
	 * Gets a StatCollection with the matching name. If it doesn't exist, returns null.
	 * @param name
	 * @return A StatCollection, or null
	 */
	public StatCollection getStats(String name) {
		for(StatCollection s : stats) {
			if(s.getName().equals(name)) {
				return s;
			}
		}
		return null;
	}
	
	public Set<StatCollection> getAllStats() {
		return stats;
	}

	public void addStatCollection(StatCollection sc) {
		this.stats.add(sc);
	}
	
	/**
	 * Return the {@link net.dv8tion.jda.core.entities.User#getIdLong() longId} of the {@link net.dv8tion.jda.core.entities.User User}.
	 * @return the id
	 */
	public long getUserId() {
		return userId;
	}
	
	/**
	 * Save the user to a user file. Once this class changes, I'm not sure it will be compatible with previous versions.
	 * @return true, if save is successful.
	 */
	public boolean saveUser() {
		File userFolder = new File(Util.BOT_PATH + File.separatorChar + "MDCUsers");
		if(!userFolder.exists())
			userFolder.mkdirs();
		File userFile = new File(userFolder.getPath() + File.separatorChar + userId + ".mdu");
		if(!userFile.exists()) {
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile));
			oos.writeObject(this);
			oos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the MDCUser with the specified id.
	 * @param userId The user id
	 * @return The MDCUser=
	 */
	public static MDCUser getMDCUser(long userId) {
		File userFolder = new File(Util.BOT_PATH + File.separatorChar + "MDCUsers");
		if(!userFolder.exists())
			userFolder.mkdirs();
		File userFile = new File(userFolder.getPath() + File.separatorChar + userId + ".mdu");
		MDCUser searchedUser;
		if(!userFile.exists()) {
			MDCUser newUser = new MDCUser(userId);
			newUser.saveUser();
			searchedUser = newUser;
		} else {
			try {
				ObjectInputStream oos = new ObjectInputStream(new FileInputStream(userFile));
				searchedUser = (MDCUser)oos.readObject();
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				searchedUser = null;
			} catch (IOException e) {
				e.printStackTrace();
				searchedUser = null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				searchedUser = null;
			}
		}
		return searchedUser;
	}
	
	
	
	
}
