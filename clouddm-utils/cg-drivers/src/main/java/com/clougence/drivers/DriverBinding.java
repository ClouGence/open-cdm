package com.clougence.drivers;

import com.clougence.utils.loader.CgClassLoader;
import com.clougence.utils.loader.ResourceLoader;

public interface DriverBinding {

    boolean isExpired();

    void bind(ClassLoader classLoader, String... imports);

    void bind(ResourceLoader resourceLoader, String... imports);

    void bind(ClassLoader classLoader);

    void bind(ResourceLoader resourceLoader);

    CgClassLoader asClassLoader();
}
