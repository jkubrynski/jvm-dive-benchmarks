package com.kubrynski.benchmarks;

import java.util.Random;

import com.kubrynski.benchmarks.exceptions.FastException;
import com.kubrynski.benchmarks.exceptions.NormalException;
import com.kubrynski.benchmarks.exceptions.Rethrower;

/**
 * @author Jakub Kubrynski
 */
public class Caller {

	private final Random random = new Random();

	private final Rethrower rethrower = new Rethrower();

	public double callMe() {
		return Math.log(random.nextDouble());
	}

	public void withNormalException() throws NormalException {
		rethrower.normal();
	}

	public void withFastException() throws FastException {
		rethrower.fast();
	}
}
