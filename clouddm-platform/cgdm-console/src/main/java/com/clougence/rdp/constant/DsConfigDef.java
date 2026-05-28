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

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;

/**
 * @author bucketli 2020/11/5 19:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DsConfigDef {

    String name() default "";

    DsConfigGroup group() default DsConfigGroup.GENERAL;

    boolean display() default true;

    I18nDsConfigMsgKeys descKey() default I18nDsConfigMsgKeys.DS_CONFIG_DESCRIPTION_EMPTY;

    boolean valueRequire() default true;

    String valueValidRegex() default "";

    String defaultValue() default "";

    String valueAdvance() default "";

    KvConfValType kvConfWebOp() default KvConfValType.TEXT;

    boolean readOnly() default true;

    boolean isSecret() default false;

}
