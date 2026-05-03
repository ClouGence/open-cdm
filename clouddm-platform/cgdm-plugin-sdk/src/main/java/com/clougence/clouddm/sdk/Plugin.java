package com.clougence.clouddm.sdk;

import java.lang.annotation.*;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Plugin {

    DataSourceType[] dsProduct() default {};

    int order() default 1;

    String[] includePackages() default {};

    String[] excludePackages() default {};
}
