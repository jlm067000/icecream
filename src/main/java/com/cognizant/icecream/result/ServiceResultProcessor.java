package com.cognizant.icecream.result;

import java.util.function.Function;

public interface ServiceResultProcessor<T>  extends Function<ServiceResult, T> {}
