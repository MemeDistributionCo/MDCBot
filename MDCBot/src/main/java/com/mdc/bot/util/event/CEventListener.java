package com.mdc.bot.util.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CEventListener extends ListenerAdapter {
	
	private MDCBot bot;
	private List<RListener> registeredListeners;
	
	public CEventListener(MDCBot b) { 
		this.bot = b;
		registeredListeners = new ArrayList<RListener>();
	}
	
	public MDCBot getBot() {
		return this.bot;
	}
	
	/**
	 * Register a listener. Only accepts {@link RListener} to prevent overlap.
	 * @param r
	 */
	public void registerListener(RListener r) {
		this.registeredListeners.add(r);
	}
	
	/**
	 * Unregister a registered listener. Returns the listener on success, null on fail.
	 * @param r
	 * @return
	 */
	public RListener unregisterListener(RListener r) {
		if(this.registeredListeners.remove(r)) return r;
		return null;
	}
	
	/**
	 * Invoke the CEvent, pass it to all listeners
	 * @param e
	 */
	public void invokeEvent(CEvent e) {
		for(RListener r : registeredListeners) {
			for (Method m : r.getClass().getMethods()) {
				//Skip anything over 1 because it should be
				//public void onCustomEvent(CustomEvent e); with no extra params
				if(m.getParameterCount() != 1) {
					continue;
				}
				//Cycle through parameter types for method (should only be one)
				for(Class<?> c : m.getParameterTypes()) {
					//If the type is the same as the event, invoke the method
					if(c == e.getClass()) {
						try {
							m.invoke(r, e);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
							System.out.println("Error passing event " + e.getClass() +" to " + r.getClass());
						}
					}
				}
			}
		}
	}
}
