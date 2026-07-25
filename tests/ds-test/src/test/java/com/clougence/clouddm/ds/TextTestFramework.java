package com.clougence.clouddm.ds;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;

public final class TextTestFramework {

    private TextTestFramework(){
    }

    public static <C extends TextTestCase> Stream<DynamicTest> dynamicTests(Collection<String> resourcePaths, Function<String, List<C>> loader,
                                                                            Function<C, DynamicTest> testFactory) {
        return resourcePaths.stream().flatMap(resourcePath -> loader.apply(resourcePath).stream()).map(testFactory);
    }
}
