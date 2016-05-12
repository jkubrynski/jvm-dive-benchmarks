package com.kubrynski.benchmarks.exceptions;

/**
 * @author Jakub Kubrynski
 */
public class FastException extends Throwable {


	private final int value;

	public FastException(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
