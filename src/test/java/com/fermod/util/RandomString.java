package com.fermod.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A class to generate pseudorandom sequences of strings.
 */
public class RandomString {

	public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
	public static final String DIGITS = "0123456789";
	public static final String ALPHANUM = UPPER + LOWER + DIGITS;

	private final Random random;
	private final char[] symbols;
	private final char[] buffer;

	/**
	 * Constructs a new random string generator instance used to create pseudorandom strings.
	 * 
	 * @param length the random generated string size
	 * @param random the class instance to generate pseudorandom strings 
	 * @param symbols the string containing all the symbols, that the random strings will be composed of
	 */
	public RandomString(int length, Random random, String symbols) {
		if (length < 1) {
			throw new IllegalArgumentException();
		}
		if (symbols.length() < 2) {
			throw new IllegalArgumentException();
		}
		this.random = Objects.requireNonNull(random);
		this.symbols = symbols.toCharArray();
		this.buffer = new char[length];
	}


	/**
	 * Constructs a new random string generator instance.
	 * The random string will be formed with alphanumeric characters.
	 * 
	 * @param length the random generated string size
	 * @param random the class instance to generate pseudorandom strings 
	 */
	public RandomString(int length, Random random) {
		this(length, random, ALPHANUM);
	}

	/**
	 * Constructs a new random string generator instance used to create pseudorandom strings from a secure generator.
	 * 
	 * @param length the random generated string size
	 * @param symbols the string containing all the symbols, that the random strings will be composed of
	 */
	public RandomString(int length, String symbols) {
		this(length, new SecureRandom(), symbols);
	}

	/**
	 * Constructs a new random string generator instance used to create pseudorandom strings from a secure generator.
	 * The random string will be formed with alphanumeric characters.
	 * 
	 * @param length the random generated string size
	 * 
	 * @see SecureRandom
	 */
	public RandomString(int length) {
		this(length, new SecureRandom());
	}

	/**
	 * Returns a pseudorandom, uniformly distributed {@code String} value, from this random string generator's 
	 * sequence. The general contract of {@code nextString} is that one {@code String} value formed with the 
	 * available symbols is pseudorandomly generated and returned. All possible {@code String} values  are produced
	 * with (approximately) equal probability. The method {@code nextString} is implemented using the provided
	 * {@code Random} class's methods.
	 * <p>
	 * The probability of a collision, {@code p}, is approximately n<sup>2</sup>/(2q<sup>x</sup>), where 
	 * {@code n} is the number of identifiers actually generated, {@code q} is the number of distinct symbols in
	 * the alphabet, and {@code x} is the length of the generated string. The longer the {@code String} is, the lower
	 * the probability of a collision taking place is.
	 * 
	 * @return the next pseudorandom, uniformly distributed {@code String} value formed with the provided symbols and
	 *         from this random generator's sequence
	 * 
	 * @see Random
	 */
	public String nextString() {
		for (int i = 0; i < buffer.length; ++i) {
			buffer[i] = symbols[random.nextInt(symbols.length)];
		}
		return new String(buffer);
	}

}
