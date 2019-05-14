package com.fermod.example;

import com.fermod.event.ValueChangeListener;
import com.fermod.observer.ObservedValue;

public class Examples {

	public static void main(String[] args) {

		ObservedValue<Integer> observedValue = new ObservedValue<>(0);
		
		// Inline listener registration using lambda
		observedValue.registerListener((oldVAlue, newValue) -> {
			System.out.println("Executed lambda inline code. Old value: " + oldVAlue + " New value: " + newValue);
		});
		
		// Equivalent inline listener registration of the lambda
		observedValue.registerListener(new ValueChangeListener<Integer>() {
			@Override
			public void onValueChanged(Integer oldValue, Integer value) {
				System.out.println("Executed inline code. Old value: " + oldValue + " New value: " + value);
			}
		});
		
		// Register a mothod using method references
		observedValue.registerListener(Examples::printValueChange);

		System.out.println("Change value to 10");
		observedValue.set(10);

	}

	public static void printValueChange(int oldValue, int newValue) {
		System.out.println("Executed printValueChange method. Old value: " + oldValue + " New value: " + newValue);
	}

}
