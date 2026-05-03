package com.clougence.clouddm.console.web.global.session;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.clougence.clouddm.console.web.constants.DmControllerUrlPrefix;
import com.clougence.clouddm.console.web.global.handler.TimeCostInterceptor;
import com.clougence.rdp.component.jwtsession.RdpJwtInterceptor;
import com.clougence.rdp.component.jwtsession.RdpJwtManager;
import com.clougence.rdp.component.mcp.McpSessionManager;
import com.clougence.rdp.component.openapi.OpenApiSessionManager;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocket
@ConditionalOnWebApplication
public class WebConfigurer implements WebMvcConfigurer, WebSocketConfigurer {

    @Resource
    private ApplicationContext applicationContext;

    @Value("${console.config.cross.origins:}")
    private String             allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        configCorsMapping(registry, "/**");
    }

    private void configCorsMapping(CorsRegistry registry, String mapping) {
        if (StringUtils.isBlank(allowedOrigins)) {
            return;
        }

        log.info("configCorsMapping mapping is " + mapping + ", allowedOrigins is " + allowedOrigins);
        CorsRegistration registration = registry.addMapping(mapping)
            .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
            .allowCredentials(true)
            .maxAge(3600)
            .allowedHeaders("*");

        if (allowedOrigins.equals("*")) {
            registration.allowedOriginPatterns(allowedOrigins);
        } else {
            registration.allowedOrigins(allowedOrigins.split(","));
        }
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(524288); // 512 KB
        container.setMaxBinaryMessageBufferSize(524288); // 512 KB
        container.setMaxSessionIdleTimeout(15 * 60000L);
        return container;
    }

    @Bean
    public RdpJwtManager dmJwtManager() {
        return new DmJwtManager();
    }

    @Bean
    public RdpJwtInterceptor dmJwtInterceptor() {
        return new RdpJwtInterceptor();
    }

    @Bean
    public OpenApiSessionManager dmApiSessionManager() {
        return new OpenApiSessionManager(DmControllerUrlPrefix.OPEN_API_PREFIX);
    }

    @Bean
    public McpSessionManager dmMcpSessionManager() {
        return new McpSessionManager(DmControllerUrlPrefix.DM_MCP_API_PREFIX);
    }

    @Bean
    public WebSoInterceptor dmWebSoInterceptor() {
        return new WebSoInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimeCostInterceptor());
        registry.addInterceptor(dmJwtInterceptor());
        registry.addInterceptor(dmApiSessionManager());
        registry.addInterceptor(dmMcpSessionManager());
    }

    @Override
    @SneakyThrows
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSoInterceptor soInterceptor = dmWebSoInterceptor();
        soInterceptor.getSocketMeta().initMetadata(this.applicationContext);
        soInterceptor.getSocketMeta().registryWebSocket(registry, soInterceptor);
    }
}
