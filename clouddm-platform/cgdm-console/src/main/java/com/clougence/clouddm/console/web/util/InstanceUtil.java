package com.clougence.clouddm.console.web.util;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2019-12-30 22:46
 * @since 1.1.3
 */
@Slf4j
public class InstanceUtil {

    /**
     * get beanName of specified class with Service or Component annotation.
     */
    public static String getSpringServiceAnnotationValue(Class clazz) {
        Annotation a = clazz.getAnnotation(Service.class);
        if (a == null) {
            a = clazz.getAnnotation(Component.class);
        }

        if (a == null) {
            throw new IllegalArgumentException("class: " + clazz.getName() + " missing Spring Service annotation");
        }

        try {
            Method method = a.annotationType().getDeclaredMethod("value");
            String beanName = (String) method.invoke(a);
            if (StringUtils.isBlank(beanName)) {
                String shortClassName = ClassUtils.getShortName(clazz);
                beanName = Introspector.decapitalize(shortClassName);
            }

            if (StringUtils.isBlank(beanName)) {
                throw new IllegalArgumentException(clazz.getName() + "'s bean name is empty!");
            } else {
                return beanName;
            }
        } catch (Throwable e) {
            String msg = "failed to get bean name,msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
