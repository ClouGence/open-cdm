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
public @interface AuthLabel {

    String category() default "";

    String i18nKey();

    String[] include() default {};

    /** System core auth must select and it is readonly */
    boolean isMustSelectAndReadOnly() default false;

    int order() default 0;

    boolean usedOfRole() default true;

    String[] tag() default {};

    AuthKind[] kind() default {};

    boolean global() default false;
}
