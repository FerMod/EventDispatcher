package com.fermod.observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.fermod.event.EventPublisher;
import com.fermod.event.ValueChangeListener;

/**
 * Class that wraps a value and gives the utility of registering listeners that will be called
 * after any variable value change.
 *
 * @param <T> the type of the observed value
 */
public class ObservedValue<T> extends EventPublisher<ValueChangeListener<T>> implements Serializable {

	private T value;

	/**
	 * Default constructor of the class {@code ObservedValue<T>}. The observed value 
	 * initialization is leaved to the compiler. This default will be {@code zero} or 
	 * {@code null}, depending on the data type.
	 */
	public ObservedValue() { }

	/**
	 * Constructor of the class {@code ObservedValue<T>} that takes as parameter the value
	 * that will be initialized the observed value.
	 * 
	 * @param value the value that will be initialized to 
	 */
	public ObservedValue(T value) {
		this.value = value;
	}

	/**
	 * Changes the value with the new given one, and notifies all the registered listeners of
	 * the change.
	 * 
	 * @param value the new value to assign
	 */
	public void set(T value) {
		set(value, true);
	}
	
	/**
	 * Changes the value with the new given one, and depending if the notification on change
	 * is enabled or not it will notify all the registered listeners.
	 * 
	 * @param value the new value to assign
	 * @param notifyChange {@code true} if the change should notify all the registered 
	 *        listeners, {@code false} otherwise
	 */
	public void set(T value, boolean notifyChange) {

		T oldValue = this.value;
		this.value = value;

		if(notifyChange) {
			// Notify the list of registered listeners
			this.notifyListeners((listener) -> listener.onValueChanged(oldValue, value));	
		}

	}

	/**
	 * Returns the value that is being observed.
	 * 
	 * @return the value
	 */
	public T get() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		if (!ObservedValue.class.isAssignableFrom(obj.getClass())) {
			return false;
		}

		@SuppressWarnings("unchecked")
		final ObservedValue<T> other = (ObservedValue<T>) obj;
		if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + (this.value != null ? this.value.hashCode() : 0);
		return hash;
	}

	/**
	 * The <code>writeObject</code> method is responsible for writing the state of the
	 * object for its particular class so that the corresponding <code>readObject</code>
	 * method can restore it. It may call the default mechanism for saving the Object's 
	 * fields calling <code>out.defaultWriteObject</code>.
	 * <p>
	 * The method does not need to concern itself with the state belonging to its 
	 * superclasses or subclasses. State is saved by writing the individual fields to the
	 * <code>ObjectOutputStream</code> using the <code>writeObject</code> method or by
	 * using the methods for primitive data types supported by<code> DataOutput</code>.
	 *
	 * @param out writes primitive data types and graphs of Java objects to an OutputStream
	 * 
	 * @throws IOException thrown when an I/O exception of some sort has occurred
	 * 
	 * @see Serializable
	 * @see ObjectOutputStream
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(value);
	}

	/**
	 * The <code>readObject</code> method is responsible for reading from the stream and
	 * restoring the classes fields. It may call <code>in.defaultReadObject</code> to invoke
	 * the default mechanism for restoring the object's non-static and non-transient fields
	 * <p>
	 * The <code>defaultReadObject</code> method uses information in the stream to assign
	 * the fields of the object saved in the stream with the correspondingly named fields
	 * in the current object.
	 * <p>
	 * This method handles the cast when the class has evolved to add new fields. The 
	 * method does not need to concern itself with the state belonging to its superclasses
	 * or subclasses. State is saved by writing the individual fields to the
	 * <code>ObjectOutputStream</code> using the <code>writeObject</code> method or by 
	 * using the methods for primitive data types supported by <code>DataOutput</code>.
	 * 
	 * @param in the ObjectInputStream that deserializes primitive data and objects previously
	 *        written using an ObjectOutputStream
	 *        
	 * @throws IOException thrown when an I/O exception of some sort has occurred
	 * @throws ClassNotFoundException thrown when an application does not found a definition 
	 *         for the class with the specified name
	 *         
	 * @see Serializable
	 * @see ObjectInputStream
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.value = (T) in.readObject();
	}

	/**
	 * Auto-generated serial version ID
	 */
	private static final long serialVersionUID = 8159940159964193507L;

}
