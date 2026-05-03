package com.clougence.rdp.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.clougence.rdp.dal.enumeration.ConfBelong;

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
