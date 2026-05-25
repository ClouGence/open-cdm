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

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.init.constant.I18nInitFieldKeys;

/**
 * Temporary non-web Spring container used during initialization tasks.
 * It lets Flyway migrations and fix tasks reuse the same Spring configuration outside the main web application.
 */
@SpringBootApplication(excludeName = "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration")
@ComponentScan(basePackages = { "com.clougence.clouddm.init.component.flyway", "com.clougence.clouddm.init.component.fixtasks", "com.clougence.clouddm.console.web",
                                "com.clougence.clouddm.console.web.*", "com.clougence.rdp", "com.clougence.clouddm.base", "com.clougence.clouddm.platform",
                                "com.clougence.clouddm.sdk", "com.clougence.clouddm.api" })
public class InitTaskApplication {

    @Bean
    public CommandLineRunner initTaskI18nResources() {
        return args -> DmI18nUtils.loadResources(I18nInitFieldKeys.class);
    }
}
