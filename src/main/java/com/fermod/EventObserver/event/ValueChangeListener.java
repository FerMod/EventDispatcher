package com.fermod.EventObserver.event;

@FunctionalInterface
public interface ValueChangeListener<T> {

	/**
	 * Called when the listened-to variable changes its value. The old value or new 
	 * value of the variable can be <code>null</code>. 
	 * 
	 * @param oldValue the old value of the variable.
	 * @param value the new value of the variable.
	 */
	void onValueChanged(T oldValue, T value);
	
}
