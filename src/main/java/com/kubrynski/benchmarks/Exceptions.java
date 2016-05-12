package com.kubrynski.benchmarks;

import java.util.concurrent.TimeUnit;

import com.kubrynski.benchmarks.exceptions.FastException;
import com.kubrynski.benchmarks.exceptions.NormalException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jakub Kubrynski
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(3)
public class Exceptions {

	private final Caller caller = new Caller();

	int value;

	@Setup
	public void setup() {
		value = 11;
	}

	@Benchmark
	public int withStacktrace() {
		try {
			throw new NormalException(value); // 6 elements
		} catch (NormalException e) {
			return e.getValue();
		}
	}

	@Benchmark
	public int withStacktraceDeep() {
		try {
			caller.withNormalException(); // 10 elements
		} catch (NormalException e) {
			return e.getValue();
		}
		return 0;
	}

	@Benchmark
	public int noStacktrace() {
		try {
			throw new FastException(value);
		} catch (FastException e) {
			return e.getValue();
		}
	}

	@Benchmark
	public int noStacktraceDeep() {
		try {
			caller.withFastException();
		} catch (FastException e) {
			return e.getValue();
		}
		return 0;
	}


	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(Exceptions.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}

}
