package com.kubrynski.benchmarks;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
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
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(1)
public class CallTypes {

	Double value;

	@Setup
	public void setup() {
		value = 1.0;
	}

	@Benchmark
	public Double baseline() {
		return provide();
	}

	@Benchmark
	public Double lambda() {
		Supplier<Double> sup = () -> provide();
		return sup.get();
	}

	@Benchmark
	public Double methodRef() {
		Supplier<Double> sup = this::provide;
		return sup.get();
	}

	@Benchmark
	@CompilerControl(CompilerControl.Mode.DONT_INLINE)
	public Double baselineNoInline() {
		return provide();
	}

	@Benchmark
	@CompilerControl(CompilerControl.Mode.DONT_INLINE)
	public Double lambdaNoInline() {
		Supplier<Double> sup = () -> provide();
		return sup.get();
	}

	@Benchmark
	@CompilerControl(CompilerControl.Mode.DONT_INLINE)
	public Double methodRefNoInline() {
		Supplier<Double> sup = this::provide;
		return sup.get();
	}

	public Double provide() {
		return value;
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(CallTypes.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}

}