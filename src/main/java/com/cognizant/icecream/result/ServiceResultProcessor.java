package com.cognizant.icecream.result;

import java.util.function.Function;

public interface ServiceResultProcessor<T,U>  extends Function<ServiceResult<T>, U> {}
