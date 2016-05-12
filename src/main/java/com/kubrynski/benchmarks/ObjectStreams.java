package com.kubrynski.benchmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jakub Kubrynski
 */
@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 5, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(3)
public class ObjectStreams {

	@Param({"100", "100000", "1000000", "1500000", "2000000"})
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
	public List<String> mapReduceStringFor() {
		ArrayList<String> result = new ArrayList<>();
		for (String element : this.strings) {
			String lower = element.toLowerCase();
			if (lower.charAt(5) > 5) {
				result.add(lower.substring(6,12));
			}
		}
		return result;
	}

	@Benchmark
	public List<String> mapReduceStringStream() {
		return processStream(strings.stream());
	}

	@Benchmark
	public List<String> mapReduceStringParallelStream() {
		return processStream(strings.parallelStream());
	}

	private List<String> processStream(Stream<String> stringStream) {
		return stringStream
				.map(String::toLowerCase)
				.filter(s -> s.charAt(5) > 5)
				.map(s -> s.substring(6, 12))
				.collect(Collectors.toList());
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(ObjectStreams.class.getSimpleName())
				.addProfiler(GCProfiler.class)
				.jvmArgs("-Xmx512m")
				.build();

		new Runner(opt).run();
	}

}