package com.kubrynski.benchmarks;

import java.util.Random;

/**
 * @author Jakub Kubrynski
 */
class ProtectedCaller {

	private final Random random = new Random();

	double callMe() {
		return Math.log(random.nextDouble());
	}

}
