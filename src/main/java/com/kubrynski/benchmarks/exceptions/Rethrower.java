package com.kubrynski.benchmarks.exceptions;

/**
 * @author Jakub Kubrynski
 */
public class Rethrower {

	private Thrower thrower = new Thrower();

	public void normal() throws NormalException {
		thrower.normal();
	}

	public void fast() throws FastException {
		thrower.fast();
	}
}
