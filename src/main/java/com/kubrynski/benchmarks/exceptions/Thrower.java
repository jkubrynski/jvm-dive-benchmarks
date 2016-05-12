package com.kubrynski.benchmarks.exceptions;

/**
 * @author Jakub Kubrynski
 */
class Thrower {

	private ExceptionProducer exceptionProducer = new ExceptionProducer();

	public void normal() throws NormalException {
		exceptionProducer.normal();
	}

	public void fast() throws FastException {
		exceptionProducer.fast();
	}
}
