package com.clougence.detectrule.lang.reflect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Documented
public @interface RuleIgnore {
}
