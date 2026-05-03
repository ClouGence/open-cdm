package com.clougence.rdp.constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;

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
