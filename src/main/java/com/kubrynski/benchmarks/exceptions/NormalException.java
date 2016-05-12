package com.kubrynski.benchmarks.exceptions;

/**
 * @author Jakub Kubrynski
 */
public class NormalException extends Throwable {

	private final int value;

	public NormalException(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
