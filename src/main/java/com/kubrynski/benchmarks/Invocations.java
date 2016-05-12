package com.kubrynski.benchmarks;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

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
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(3)
public class Invocations {

	private static final String CALL_ME = "callMe";
	private Caller caller = new Caller();

	private MethodHandle dynamicMethod;
	private Method reflectMethod;

	@Setup
	public void setup() throws NoSuchMethodException, IllegalAccessException {
		reflectMethod = Caller.class.getMethod(CALL_ME);
		dynamicMethod = MethodHandles.lookup().findVirtual(Caller.class, CALL_ME, MethodType.methodType(double.class));
	}

	@Benchmark
	public double baseline() {
		return caller.callMe();
	}

	@Benchmark
	public Object reflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Method directCall = Caller.class.getMethod(CALL_ME);
		return directCall.invoke(caller);
	}

	@Benchmark
	public Object dynamic() throws Throwable {
		MethodHandle directCall = MethodHandles.lookup().findVirtual(Caller.class, CALL_ME, MethodType.methodType(double.class));
		return directCall.invoke(caller);
	}

	@Benchmark
	public Object reflectWithoutLookup() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return reflectMethod.invoke(caller);
	}

	@Benchmark
	public Object dynamicWithoutLookup() throws Throwable {
		return dynamicMethod.invoke(caller);
	}

	@Benchmark
	public double dynamicExactWithoutLookup() throws Throwable {
		return (double) dynamicMethod.invokeExact(caller);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(Invocations.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}

}