package com.clougence.clouddm.console.web.global.rsocket;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.comm.component.RSocketApiFactoryBean;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * Used to offer rsocket api interface to spring create time is 2021/1/15
 **/
@FieldNameConstants
@Data
public class DmServerApiFactoryBean<T> implements RSocketApiFactoryBean {

    private Class<T>           interfaceClass;

    private ApplicationContext applicationContext;

    @Override
    public T getObject() throws Exception {
        Object object = DmServerRSocketApiProxy.registerApi(interfaceClass, applicationContext);
        return (T) object;
    }

    @Override
    public Class<?> getObjectType() { return interfaceClass; }

    @Override
    public boolean isSingleton() { return true; }
}
