package com.clougence.utils.reflect.annotations;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.clougence.utils.reflect.Annotations;

public class BasicTest {

    @Test
    public void test() throws IOException {
        Annotations annotations = Annotations.ofClass(TestBean.class);
        assert annotations.getAnnotation(ResultMap.class) != null;
    }

    @ResultMap()
    class TestBean {
    }
}
