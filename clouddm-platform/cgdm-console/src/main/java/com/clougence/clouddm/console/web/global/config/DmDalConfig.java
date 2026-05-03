package com.clougence.clouddm.console.web.global.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.clougence.clouddm.console.web.constants.DmMode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/10/25 19:49:05
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.clougence.clouddm.console.web.dal.mapper", "com.clougence.rdp.dal.mapper" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class DmDalConfig {

    private static final String[] MAPPER_LOCATIONS = { "classpath:/mybatis/mapper/*.xml" };

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @ConditionalOnExpression("#{'output'.equalsIgnoreCase(environment['clouddm.mode.type'])}")
    public DataSource defaultDataSource() {
        log.info("Default HikariCP datasource inited.");
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, DmConsoleConfig dmConfig) throws Exception {
        final MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(resolveMapperLocations());
        factoryBean.setPlugins(mybatisPlusInterceptor(dmConfig));
        return factoryBean.getObject();
    }

    private Resource[] resolveMapperLocations() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> mapperResources = new ArrayList<>();
        for (String mapperLocation : MAPPER_LOCATIONS) {
            Collections.addAll(mapperResources, resolver.getResources(mapperLocation));
        }
        return mapperResources.toArray(new Resource[0]);
    }

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(DmConsoleConfig dmConfig) {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setMaxLimit(-1L);
        paginationInterceptor.setDbType(dmConfig.getDmMode() == DmMode.desktop ? DbType.H2 : DbType.MYSQL);
        paginationInterceptor.setOptimizeJoin(true);
        return paginationInterceptor;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(DmConsoleConfig dmConfig) {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.setInterceptors(Collections.singletonList(paginationInnerInterceptor(dmConfig)));
        return mybatisPlusInterceptor;
    }
}
