package com.clougence.clouddm.worker.component.rsocket;

import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;

import com.clougence.clouddm.comm.component.RSocketApiFactoryBean;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * @author wanshao create time is 2021/1/16
 **/
@Data
@FieldNameConstants
public class DmClientApiFactoryBean<T> implements RSocketApiFactoryBean {

    private Class<T>           interfaceClass;
    private ApplicationContext applicationContext;

    @Override
    public T getObject() {
        ClassLoader classLoader = interfaceClass.getClassLoader();
        Class<?>[] interfaces = new Class[] { interfaceClass };
        DmClientRSocketApiProxy proxy = new DmClientRSocketApiProxy(interfaceClass, applicationContext, RSocketSerializationImpl.DEFAULT);
        return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
    }

    @Override
    public Class<?> getObjectType() { return interfaceClass; }

    @Override
    public boolean isSingleton() { return true; }
}
