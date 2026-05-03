package com.clougence.rdp.util;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

/**
 * @author bucketli 2019-12-30 22:46
 * @since 1.1.3
 */
public class RdpInstanceUtil {

    /**
     * get beanName of specified class with Service or Component annotation.
     */
    public static String getSpringServiceAnnotationValue(Class clazz) {
        Annotation a = clazz.getAnnotation(Service.class);
        if (a == null) {
            a = clazz.getAnnotation(Component.class);
        }

        if (a == null) {
            throw new IllegalArgumentException(String.format("class: %s missing Spring Service annotation", clazz.getName()));
        }

        try {
            Method method = a.annotationType().getDeclaredMethod("value");
            String beanName = (String) method.invoke(a);
            if (StringUtils.isBlank(beanName)) {
                String shortClassName = ClassUtils.getShortName(clazz);
                beanName = Introspector.decapitalize(shortClassName);
            }

            if (StringUtils.isBlank(beanName)) {
                throw new RuntimeException(clazz.getName() + "'s bean name is empty!");
            }

            return beanName;
        } catch (Throwable e) {
            String msg = "get bean name error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            throw new RuntimeException(msg, e);
        }
    }
}
