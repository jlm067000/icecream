package com.cognizant.icecream.result;

import java.util.function.Function;

public interface ResultProcessor<U> extends Function<Result,U> {}
