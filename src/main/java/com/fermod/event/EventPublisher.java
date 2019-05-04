package com.fermod.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * Class that allows to register and notify event listeners.
 *
 * @param <T> the listener type that will provide the notification method 
 */
public abstract class EventPublisher<T> {

	private final transient ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
	protected final transient Lock readLock = readWriteLock.readLock();
	protected final transient Lock writeLock = readWriteLock.writeLock();

	private transient List<T> listeners = new ArrayList<>();

	/**
	 * Adds the specified listener to the list of registered listeners.
	 * 
	 * @param listener the listener that will be called
	 * @return the registered listener instance
	 */
	public T registerListener(T listener) {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {
			// Add the listener to the list of registered listeners
			this.listeners.add(listener);
		} finally {
			// Unlock the writer lock
			this.writeLock.unlock();
		}

		return listener;
	}

	/**
	 * Removes the first occurrence of the specified listener from the list of the registered listeners.
	 * 
	 * @param listener the listener that should be removed from the list
	 */
	public void unregisterListener(T listener) {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {
			// Remove the listener from the list of the registered listeners
			this.listeners.remove(listener);
		} finally {
			// Unlock the writer lock
			this.writeLock.unlock();
		}

	}

	/**
	 * Removes all the listeners from the list of the registered listeners. The list will be empty after this call returns.
	 */
	public void unregisterAllListeners() {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {
			// Remove all the listeners from the list of the registered listeners
			this.listeners.clear();
		} finally {
			// Unlock the writer lock
			this.writeLock.unlock();
		}

	}

	/**
	 * Notifies and executes on each of the registered listeners the provided function.
	 * 
	 * @param action operation that accepts a single input argument and returns no result
	 */
	public void notifyListeners(Consumer<? super T> action) {

		// Lock the list of listeners for reading
		this.readLock.lock();

		try {
			// Execute some function on each of the listeners
			this.listeners.forEach(action);
		} finally {
			// Unlock the reader lock
			this.readLock.unlock();
		}

	}

}
