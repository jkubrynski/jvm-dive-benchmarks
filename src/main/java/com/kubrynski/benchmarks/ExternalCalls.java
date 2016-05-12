package com.kubrynski.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Jakub Kubrynski
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(3)
public class ExternalCalls {

	Blackhole blackhole = new Blackhole();

	Caller caller = new Caller();
	ProtectedCaller protectedCaller = new ProtectedCaller();

	@Benchmark
	public void baseline() {
		directCall(caller.callMe());
	}

	@Benchmark
	public void lambda() {
		supplierCall(() -> caller.callMe());
	}

	@Benchmark
	public void methodRef() {
		supplierCall(caller::callMe);
	}

	@Benchmark
	public void baselineProtected() {
		directCall(protectedCaller.callMe());
	}

	@Benchmark
	public void lambdaProtected() {
		supplierCall(() -> protectedCaller.callMe());
	}

	@Benchmark
	public void methodRefProtected() {
		supplierCall(protectedCaller::callMe);
	}

	private void supplierCall(Supplier<Double> sup) {
		blackhole.consume(sup.get());
	}

	private void directCall(Double sup) {
		blackhole.consume(sup);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(ExternalCalls.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}

}