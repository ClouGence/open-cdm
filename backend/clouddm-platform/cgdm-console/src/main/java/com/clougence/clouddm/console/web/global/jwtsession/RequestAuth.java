package com.clougence.clouddm.console.web.global.jwtsession;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.clougence.clouddm.base.metadata.rdp.enumeration.ResourceType;
import com.clougence.clouddm.platform.dal.model.monitor.SecurityLevel;

/**
 * Attention!!! The annotation only support method that return ResponseData type
 *
 * @author mode create time is 2020/4/16
 **/
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestAuth {

    // Contains any one of them,RoleAuthLabelName
    String[] value() default {};

    AuthStrategy strategy() default AuthStrategy.RefRoleSet;

    SecurityLevel level() default SecurityLevel.NORMAL;

    ResourceType resType() default ResourceType.PURE_URL;

    boolean checkOpPassword() default false;

    String failedRedirectUrlTo() default "";

    enum AuthStrategy {
        // Ignore check
        Ignore,
        RefAnyOnes,
        // match any UserRoleType on user.
        RefRoleSet
    }
}
