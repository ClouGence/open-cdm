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

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.component.RSocketApiScanner;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;
import com.clougence.utils.loader.CgResourceScanner;
import com.clougence.utils.loader.providers.ClassPathResourceLoader;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/16
 **/
@Slf4j
@Component("dmClientApiScanner")
public class DmClientApiScanner implements RSocketApiScanner {

    private final String       SCAN_PACKAGE = "com.clougence.clouddm.api";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException { this.applicationContext = applicationContext; }

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        String appMode = SystemUtils.getSystemProperty("app.mode", "distributed");
        if (StringUtils.equalsIgnoreCase(appMode, "embedded")) {
            log.info("-Dapp.mode=embedded");
            return;
        } else {
            log.info("load RSocket...");
        }

        ClassLoader appClassLoader = this.applicationContext.getClassLoader();
        for (String className : getAnnotatedClass(appClassLoader, SCAN_PACKAGE)) {
            Class<?> clazz = appClassLoader.loadClass(className);
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.getPropertyValues().add(DmClientApiFactoryBean.Fields.interfaceClass, clazz);
            definition.getPropertyValues().add(DmClientApiFactoryBean.Fields.applicationContext, applicationContext);
            definition.setBeanClass(DmClientApiFactoryBean.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionRegistry.registerBeanDefinition(clazz.getName(), definition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    /** Scan all rsocket api class */
    private Set<String> getAnnotatedClass(ClassLoader appClassLoader, String scanPackage) {
        CgResourceScanner scanner = new CgResourceScanner(new ClassPathResourceLoader(appClassLoader, ""));
        return scanner.getClassNamesSet(new String[] { SCAN_PACKAGE }, context -> {
            if (context.getClassInfo() != null) {
                for (String anno : context.getClassInfo().annos) {
                    if (anno.equals(RSocketApiClass.class.getName())) {
                        return true;
                    }
                }
            }
            return false;
        });
    }
}
