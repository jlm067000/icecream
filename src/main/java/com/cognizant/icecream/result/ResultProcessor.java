package com.cognizant.icecream.result;

import java.util.function.Function;

public interface ResultProcessor<T extends Result, U> extends Function<T,U> {}
