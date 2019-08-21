package com.cognizant.icecream.services;

import java.util.Optional;
import java.util.function.Function;

class ServicesUtil {

    static <T, U> T extractOptionally(U parameter, Function<U, Optional<T>> extractor) {

        Optional<T> optional = extractor.apply(parameter);

        return (optional.isPresent()) ? optional.get() : null;
    }
}
