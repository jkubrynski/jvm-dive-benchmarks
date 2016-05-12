package com.kubrynski.benchmarks.dynamic;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @author Jakub Kubrynski
 */
public class Bootstrapper {
	public static CallSite bootstrap(Object... args) throws Throwable {
		MethodType methodType = MethodType.methodType(Integer.class, Integer.class);
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle methodHandle = lookup.findStatic(Multiplier.class, "staticMultiply", methodType);
		return new ConstantCallSite(methodHandle);
	}
}
