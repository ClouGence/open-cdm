package com.clougence.clouddm.base.metadata.ds;

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
public @interface ConfigDef {

    String name() default "";

    DsConfigGroup group() default DsConfigGroup.GENERAL;

    boolean display() default true;

    ConfigI18nKey descKey() default ConfigI18nKey.CONFIG_DESCRIPTION_EMPTY;

    boolean valueRequire() default true;

    String valueValidRegex() default "";

    String defaultValue() default "";

    String valueAdvance() default "";

    boolean readOnly() default true;

    boolean isSecret() default false;

}
