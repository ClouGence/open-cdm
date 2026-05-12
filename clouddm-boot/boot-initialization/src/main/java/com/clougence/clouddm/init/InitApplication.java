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
 */
package com.clougence.clouddm.init;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.clougence.clouddm.console.web.global.handler.StaticResourceNoCacheFilter;
import com.clougence.clouddm.console.web.global.exception.PrintErrorUncaughtExcHandler;
import com.clougence.clouddm.init.constant.I18nInitFieldKeys;
import com.clougence.clouddm.console.web.util.DmI18nUtils;

import jakarta.servlet.Filter;
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
                                "com.clougence.clouddm.console.web.constants",
                                "com.clougence.clouddm.console.web.global.exception", "com.clougence.clouddm.api.common.rpc",
                                "com.clougence.rdp.constant.auth" }, excludeFilters = { @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.clougence\\.clouddm\\.console\\.web\\.service\\.system\\.impl\\..*") })
public class InitApplication implements WebMvcConfigurer {

    @PostConstruct
    public void initI18nResources() {
        DmI18nUtils.loadResources(I18nInitFieldKeys.class);
    }

    @Bean
    public FilterRegistrationBean<Filter> indexHtmlNoCacheFilter() {
        return StaticResourceNoCacheFilter.indexHtml("/");
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new PrintErrorUncaughtExcHandler());
        System.setProperty("server.port", "8222");
        System.setProperty("spring.config.name", "init");
        System.setProperty("spring.profiles.active", "init");
        System.setProperty("spring.web.resources.static-locations", resolveStaticLocations());
        System.setProperty("spring.autoconfigure.exclude", "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,"
                                                           + "org.springframework.boot.jdbc.autoconfigure.DataSourceTransactionManagerAutoConfiguration,"
                                                           + "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration,"
                                                           + "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration");
        SpringApplication.run(InitApplication.class, args);

        log.info("[DmAloneLauncher] Alone All Context Inited.");
        ShutdownHook.joinShutdown();
    }

    private static String resolveStaticLocations() {
        List<String> locations = new ArrayList<>();
        Path workspaceDir = Paths.get(System.getProperty("user.dir"));
        addStaticLocationIfPresent(locations, workspaceDir.resolve("clouddm-platform/cgdm-web/build/resources/main/templates"));
        addStaticLocationIfPresent(locations, workspaceDir.resolve("clouddm-platform/cgdm-web/dist/templates"));
        locations.add("classpath:/templates/");
        return String.join(",", locations);
    }

    private static void addStaticLocationIfPresent(List<String> locations, Path directory) {
        if (!Files.isDirectory(directory)) {
            return;
        }
        String location = directory.toAbsolutePath().normalize().toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        locations.add(location);
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
