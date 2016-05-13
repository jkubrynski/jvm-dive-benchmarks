package com.kubrynski.benchmarks.dynamic;

import java.util.concurrent.TimeUnit;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.InvokeDynamic;
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

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * @author Jakub Kubrynski
 */
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Fork(3)
public class InvokeDynamicBenchmark {

	public Integer value;

	private Calculator invokeDynamic;

	private Calculator invokeStatic;

	private Calculator invokeVirtual;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(InvokeDynamicBenchmark.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}

	@Setup
	public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
		value = 1;

		Implementation bootstrap = InvokeDynamic.bootstrap(Bootstrapper.class.getDeclaredMethod("bootstrap", Object[].class))
				.withArgument(0);

		this.invokeDynamic = new ByteBuddy()
				.subclass(Calculator.class)
				.method(named("calc")).intercept(bootstrap).make()
				.load(Invoker.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded().newInstance();

		this.invokeStatic = new Calculator() {
			@Override
			public Integer calc(final Integer value) {
				return Multiplier.staticMultiply(value);
			}
		};

		this.invokeVirtual = new Calculator() {

			Multiplier delgate = new Multiplier();

			@Override
			public Integer calc(final Integer value) {
				return delgate.multiply(value);
			}
		};
	}

	@Benchmark
	public Integer invokeStatic() {
		return invokeStatic.calc(value);
	}

	@Benchmark
	public Integer invokeVirtual() {
		return invokeVirtual.calc(value);
	}

	@Benchmark
	public Integer invokeDynamic() {
		return invokeDynamic.calc(value);
	}

}