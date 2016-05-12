package com.kubrynski.benchmarks.exceptions;

/**
 * @author Jakub Kubrynski
 */
public class ExceptionProducer {

	public int value;

	public ExceptionProducer() {
		value = 11;
	}

	public void normal() throws NormalException {
		throw new NormalException(value);
	}

	public void fast() throws FastException {
		throw new FastException(value);
	}
}
