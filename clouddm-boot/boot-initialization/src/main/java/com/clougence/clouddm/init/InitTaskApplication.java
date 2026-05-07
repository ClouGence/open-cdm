package com.clougence.clouddm.init;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 初始化阶段临时非 Web 任务容器。
 * 供 Flyway 和 fix 任务复用同一套 Spring 配置。
 */
@SpringBootApplication(excludeName = "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration")
@ComponentScan(basePackages = { "com.clougence.clouddm.init.component.flyway", "com.clougence.clouddm.init.component.fixtasks", "com.clougence.clouddm.console.web",
                                "com.clougence.clouddm.console.web.*", "com.clougence.rdp", "com.clougence.clouddm.base", "com.clougence.clouddm.platform",
                                "com.clougence.clouddm.sdk", "com.clougence.clouddm.api" })
public class InitTaskApplication {
}
