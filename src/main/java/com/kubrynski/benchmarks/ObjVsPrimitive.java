package com.kubrynski.benchmarks;

import java.lang.invoke.MethodHandles;
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
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jakub Kubrynski
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(3)
public class ObjVsPrimitive {

	@Param({"10000", "100000", "1000000"})
	int size;

	Integer[] objects;

	Integer[] objectsRandom;

	int[] primitives;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(MethodHandles.lookup().lookupClass().getSimpleName())
				.build();

		new Runner(opt).run();
	}

	@Setup
	public void setup(Blackhole blackhole) {
		objects = new Integer[size];
		objectsRandom = new Integer[size];
		primitives = new int[size];

		Random random = new Random(123);
		for (int i = 0; i < size; i++) {
			int toAdd = random.nextInt();
			objects[i] = toAdd;
			primitives[i] = toAdd;
		}

		random = new Random(123);
		Random trashRandom = new Random(345);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < trashRandom.nextInt(3); j++) {
				blackhole.consume(new Integer(trashRandom.nextInt()));
			}
			objectsRandom[i] = random.nextInt();
		}
	}

	@Benchmark
	public int primitives() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += primitives[i];
		}
		return sum;
	}

	@Benchmark
	public int objects() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += objects[i];
		}
		return sum;
	}

	@Benchmark
	public int objectsRandom() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum += objectsRandom[i];
		}
		return sum;
	}

}
