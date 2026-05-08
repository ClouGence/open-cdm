package com.clougence.clouddm.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.clougence.clouddm.console.web.global.exception.PrintErrorUncaughtExcHandler;
import com.clougence.clouddm.init.constant.I18nInitFieldKeys;
import com.clougence.rdp.util.RdpI18nUtils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Boot entry point for initialization mode.
 * This application starts the lightweight initialization web flow and then blocks until the JVM begins shutdown,
 * so the init process does not exit immediately after the Spring context is created.
 */
@Slf4j
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@ComponentScan(basePackages = { "com.clougence.clouddm.init.controller", "com.clougence.clouddm.init.service", "com.clougence.clouddm.init.model",
                                "com.clougence.clouddm.console.web.constants", "com.clougence.clouddm.console.web.service.system",
                                "com.clougence.clouddm.console.web.global.exception", "com.clougence.clouddm.api.common.rpc",
                                "com.clougence.rdp.constant.auth" }, excludeFilters = { @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.clougence\\.clouddm\\.console\\.web\\.service\\.system\\.impl\\..*") })
public class InitApplication implements WebMvcConfigurer {

    @PostConstruct
    public void initI18nResources() {
        RdpI18nUtils.loadResources(I18nInitFieldKeys.class);
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
        System.setProperty("server.port", "8222");
        System.setProperty("spring.config.name", "init");
        System.setProperty("spring.profiles.active", "init");
        System.setProperty("spring.web.resources.static-locations", "classpath:/templates");
        System.setProperty("spring.autoconfigure.exclude", "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
                                                           + "org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration,"
                                                           + "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration,"
                                                           + "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration");
        SpringApplication.run(InitApplication.class, args);

        log.info("[DmAloneLauncher] Alone All Context Inited.");
        ShutdownHook.joinShutdown();
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
