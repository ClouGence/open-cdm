package com.clougence.utils.i18n;

import java.lang.annotation.*;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface I18nResource {

    String[] value() default {};
}
