package com.clougence.detectrule.lang.reflect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface RuleFunction {

    String value() default "";
}
