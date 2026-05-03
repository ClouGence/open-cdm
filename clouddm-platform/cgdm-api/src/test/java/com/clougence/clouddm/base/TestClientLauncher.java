package com.clougence.clouddm.base;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import com.clougence.clouddm.comm.ConsoleRSocketServer;

/**
 * @author wanshao create time is 2021/1/8
 **/
@EnableAutoConfiguration
@SpringBootConfiguration
@ComponentScan(value = { "com.clougence.clouddm.*" }, excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConsoleRSocketServer.class) })
public class TestClientLauncher {

    public static void main(String[] args) {
        SpringApplication.run(TestServerLauncher.class, args);
    }
}
