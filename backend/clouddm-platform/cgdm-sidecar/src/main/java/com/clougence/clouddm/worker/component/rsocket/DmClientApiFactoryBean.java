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
 */package com.clougence.clouddm.worker.component.rsocket;

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
