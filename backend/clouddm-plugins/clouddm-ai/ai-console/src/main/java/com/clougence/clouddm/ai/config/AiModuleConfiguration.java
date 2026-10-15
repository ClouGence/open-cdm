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
package com.clougence.clouddm.ai.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.clougence.clouddm.ai.controller.AiStreamInterceptor;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;

@Configuration
@ComponentScan("com.clougence.clouddm.ai")
@MapperScan(basePackages = "com.clougence.clouddm.ai.dao.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class AiModuleConfiguration implements WebMvcConfigurer {
    @Resource
    private AiStreamInterceptor streamInterceptor;

    @PostConstruct
    public void loadMessages() {
        DmI18nUtils.loadResources(AiModuleConfiguration.class.getClassLoader(), "/ai/request-label", "/ai/messages");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(streamInterceptor).addPathPatterns("/api/entry/ai/turn/streamTurn", "/api/entry/ai/turn/resumeTurn");
    }
}
