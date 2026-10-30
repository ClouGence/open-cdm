/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.global.jwtsession;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.clougence.clouddm.platform.dal.model.ResourceType;
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
