package com.clougence.clouddm.boot.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan({ "com.clougence.clouddm.boot", "com.clougence.clouddm.worker", "com.clougence.clouddm.worker.*", "com.clougence.clouddm.comm.*" })
public class FullAppConfig {
}