package com.clougence.clouddm.sdk.execute.dsconf;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface Serialization {

    String provider();
}
