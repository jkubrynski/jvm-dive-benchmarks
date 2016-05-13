package com.kubrynski.benchmarks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
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
@Warmup(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(1)
public class Fors {

	@Param({"10000", "100000"})
	int size;

	List<String> strings = new ArrayList<>(size);

	@Setup
	public void setup() {
		Random random = new Random(123);
		for (int i = 0; i < size; i++) {
			strings.add("abc" + random.nextDouble() + "def");
		}
	}

	@Benchmark
	public List<String> forStatement() {
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < strings.size(); i++) {
			result.add(strings.get(i));
		}
		return result;
	}

	@Benchmark
	public List<String> forIterStatement() {
		ArrayList<String> result = new ArrayList<>();
		Iterator<String> iterator = strings.iterator();
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}
		return result;
	}

	@Benchmark
	public List<String> foreachStatement() {
		ArrayList<String> result = new ArrayList<>();
		for (String element : strings) {
			result.add(element);
		}
		return result;
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(Fors.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}

}