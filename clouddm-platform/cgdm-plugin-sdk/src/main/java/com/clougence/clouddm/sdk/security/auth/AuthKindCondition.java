package com.clougence.clouddm.sdk.security.auth;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author mode create time is 2021/2/23
 **/
@Repeatable(AuthKindConditionSet.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthKindCondition {

    AuthKind kind();

    AuthElementType[] condition();
}
