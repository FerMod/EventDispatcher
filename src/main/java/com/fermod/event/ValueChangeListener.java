package com.fermod.event;

/**
 * The listener interface for receiving value change events.
 * 
 * <p>This is a {@link FunctionalInterface functional interface} whose functional method is {@link #onValueChanged}.
 *
 * @param <T> the type of the variable that is being listened to
 */
@FunctionalInterface
public interface ValueChangeListener<T> {

	/**
	 * Function called when the listened to variable value change.
	 * 
	 * @param oldValue the old value of the variable
	 * @param value the new value of the variable
	 */
	void onValueChanged(T oldValue, T value);

}
