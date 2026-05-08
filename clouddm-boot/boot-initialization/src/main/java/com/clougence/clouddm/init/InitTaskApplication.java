package com.clougence.clouddm.init;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Temporary non-web Spring container used during initialization tasks.
 * It lets Flyway migrations and fix tasks reuse the same Spring configuration outside the main web application.
 */
@SpringBootApplication(excludeName = "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration")
@ComponentScan(basePackages = { "com.clougence.clouddm.init.component.flyway", "com.clougence.clouddm.init.component.fixtasks", "com.clougence.clouddm.console.web",
                                "com.clougence.clouddm.console.web.*", "com.clougence.rdp", "com.clougence.clouddm.base", "com.clougence.clouddm.platform",
                                "com.clougence.clouddm.sdk", "com.clougence.clouddm.api" })
public class InitTaskApplication {
}
