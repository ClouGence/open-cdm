package com.clougence.clouddm.sdk.security.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mode create time is 2021/2/23
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCategory {

    String i18nKey();

    String parent() default "";

    boolean hidden() default false;

    int order() default 0;
}
