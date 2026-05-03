package com.clougence.clouddm.console.web.global.rsocket;

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
 * Scan all rsocket api interface and offer all their proxy object to spring
 *
 * @author wanshao create time is 2021/1/15
 **/

@Slf4j
@Component
public class DmServerApiScanner implements RSocketApiScanner {

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

        for (String className : getAnnotatedClass()) {
            Class<?> clazz = applicationContext.getClassLoader().loadClass(className);
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

            definition.getPropertyValues().add(DmServerApiFactoryBean.Fields.interfaceClass, clazz);
            definition.getPropertyValues().add(DmServerApiFactoryBean.Fields.applicationContext, applicationContext);

            definition.setBeanClass(DmServerApiFactoryBean.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionRegistry.registerBeanDefinition(clazz.getName(), definition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    /** Scan all rsocket api class */
    private Set<String> getAnnotatedClass() {
        String SCAN_PACKAGE = "com.clougence.clouddm.api";
        CgResourceScanner scanner = new CgResourceScanner(new ClassPathResourceLoader(applicationContext.getClassLoader(), ""));
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
