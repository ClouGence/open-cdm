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
package com.clougence.rdp.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.clougence.clouddm.console.web.global.i18n.I18nUserConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.ConfBelong;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;

/**
 * @author bucketli 2020/11/5 19:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UserConfigDef {

    String name() default "";

    I18nUserConfigMsgKeys descKey() default I18nUserConfigMsgKeys.USER_CONFIG_EMPTY;

    String defaultValue() default "";

    String valueRange() default "";

    boolean readOnly() default false;

    UserConfigTagType configTagType() default UserConfigTagType.COMMON;

    KvConfValType kvConfWebOp() default KvConfValType.TEXT;

    ConfBelong confBelong() default ConfBelong.Common;

    boolean isSecret() default false;
}
