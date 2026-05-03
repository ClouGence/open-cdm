package com.clougence.rdp.component.mcp.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ FIELD, TYPE })
@Retention(RUNTIME)
public @interface McpField {

    String value();

    boolean required() default false;

    String alias() default "";
}
