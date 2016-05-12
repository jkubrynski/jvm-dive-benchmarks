package com.kubrynski.benchmarks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
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
public class PrimitiveStreams {

	static final int SIZE = 1_000_000;

	Blackhole blackhole = new Blackhole();

	int[] array;

	@Setup
	public void setup() {
		array = new int[SIZE];
		Random random = new Random(123);
		for (int i = 0; i < SIZE; i++) {
			array[i] = random.nextInt();
		}
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public void consumeWithFor() {
		for (int i : array) {
			blackhole.consume(i);
		}
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public void consumeWithForEach() {
		Arrays.stream(array).forEach(blackhole::consume);
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public List<Integer> collectWithFor() {
		List<Integer> result = new ArrayList<>();
		for (int i : array) {
			result.add(i);
		}
		return result;
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public List<Integer> collectWithForEach() {
		return Arrays.stream(array).boxed().collect(Collectors.toList());
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public int sumWithFilter() {
		int sum = 0;
		for (int i : array) {
			if (i % 2 == 0) {
				sum += i * 2;
			}
		}
		return sum;
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public int sumWithFilterStream() {
		return Arrays.stream(array).filter(value -> value % 2 == 0).map(i -> i * 2).sum();
	}

	@Benchmark
	@OperationsPerInvocation(SIZE)
	public int sumWithFilterStreamParallel() {
		return Arrays.stream(array).parallel().filter(value -> value % 2 == 0).map(i -> i * 2).sum();
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(PrimitiveStreams.class.getSimpleName())
				.addProfiler(GCProfiler.class)
				.jvmArgs("-Xmx64m")
				.build();

		new Runner(opt).run();
	}

}