package com.kubrynski.benchmarks.dynamic;

/**
 * @author Jakub Kubrynski
 */
class Multiplier {

	public static Integer staticMultiply(final Integer value) {
		return value * 2;
	}

	public Integer multiply(final Integer value) {
		return value * 2;
	}
}
