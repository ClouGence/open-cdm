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
package com.clougence.clouddm.platform.dal.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.*;
import com.clougence.drivers.def.ResDef;
import com.clougence.drivers.def.VerDef;
import com.clougence.utils.StringUtils;
import com.clougence.utils.loader.CgClassLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2023/10/25 19:49:05
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.clougence.clouddm.platform.dal.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class DmDalConfig {
    public static final String MYSQL_DRIVER_RUNTIME_FAMILY   = "cgdm-runtime-mysql";
    public static final String MYSQL_DRIVER_VERSION          = "default";
    public static final String MYSQL_DRIVER_CLASS_NAME       = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_DRIVER_MAVEN_COORDINATE = "com.mysql:mysql-connector-j:jar:8.0.33";

    @Primary
    @Bean(name = "dataSource")
    public DataSource defaultDataSource(Environment env) {
        String jdbcUrl = env.getProperty("spring.datasource.jdbcurl");
        String jdbcUser = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        long connectionTimeout = env.getProperty("spring.datasource.connection-timeout", Long.class, 10000L);
        Integer minimumIdle = env.getProperty("spring.datasource.minimum-idle", Integer.class, 1);
        Integer maximumPoolSize = env.getProperty("spring.datasource.maximum-pool-size", Integer.class, 20);

        jdbcUrl = StringUtils.trimToNull(jdbcUrl);
        jdbcUser = StringUtils.trimToNull(jdbcUser);
        if (jdbcUrl == null || jdbcUser == null) {
            throw new IllegalArgumentException("jdbcUrl/username is blank.");
        }

        // dsFactory
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(createDriverDataSource(jdbcUrl, jdbcUser, StringUtils.defaultString(password), connectionTimeout));
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        log.info("Default HikariCP datasource inited.");
        return new HikariDataSource(hikariConfig);
    }

    public static Connection createDriverConnection(String jdbcUrl, String username, String password, long connectionTimeout) throws SQLException {
        DataSource bridge = createDriverDataSource(jdbcUrl, username, password, connectionTimeout);
        return bridge.getConnection();
    }

    public static DataSource createDriverDataSource(String jdbcUrl, String username, String password, long connectionTimeout) {
        // dsFactory
        DriverLoader loader = PluginManager.driverLoader();
        DriverVersion ver = DmDalConfig.mainDsDriverVersion();
        if (!ver.isPrepared()) {
            throw new IllegalStateException("Runtime MySQL driver is not ready. family=" + MYSQL_DRIVER_RUNTIME_FAMILY + ", version=" + MYSQL_DRIVER_VERSION);
        }

        DriverBinding binding = loader.createBinding(DmDalConfig.class.getClassLoader(), MYSQL_DRIVER_RUNTIME_FAMILY, MYSQL_DRIVER_VERSION);
        if (binding == null) {
            throw new IllegalStateException("MySQL driver binding is unavailable. family=" + MYSQL_DRIVER_RUNTIME_FAMILY + ", version=" + MYSQL_DRIVER_VERSION);
        }
        if (!isDriverClassAvailable(binding)) {
            throw new IllegalStateException("Runtime MySQL driver class is unavailable.");
        }

        CgClassLoader classLoader = binding.asClassLoader();
        DmDalConfigDsFactory dsFactory = new DmDalConfigDsFactory(classLoader);

        // Hikari
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.CUSTOM_URL.getConfigKey(), jdbcUrl);
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), username);
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), StringUtils.defaultString(password));
        properties.setProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey(), Long.toString(connectionTimeout));

        return new DataSourceBridge(properties, dsFactory);
    }

    public static boolean isDriverClassAvailable(DriverBinding binding) {
        if (binding == null) {
            return false;
        }

        try {
            binding.asClassLoader().loadClass(MYSQL_DRIVER_CLASS_NAME);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static DriverVersion mainDsDriverVersion() {
        DriverLoader internalLoader = PluginManager.driverLoader();
        DriverFamily driverFamily = internalLoader.findDriver(MYSQL_DRIVER_RUNTIME_FAMILY);
        if (driverFamily == null) {
            driverFamily = internalLoader.addDriver(MYSQL_DRIVER_RUNTIME_FAMILY);
        }

        VerDef ver = (VerDef) driverFamily.findVersion(MYSQL_DRIVER_VERSION);
        if (ver == null) {
            ver = (VerDef) driverFamily.addVersion(MYSQL_DRIVER_VERSION);
            ver.setLocalDir(internalLoader.getDriverHome());
            ver.setDsFactory(DmDalConfigDsFactory.class.getName());

            ResDef resource = new ResDef();
            resource.setResourceType("maven");
            resource.setCoordinate(MYSQL_DRIVER_MAVEN_COORDINATE);
            ver.addResource(resource);
        }

        return ver;
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(resolveMapperLocations());
        factoryBean.setPlugins(mybatisInterceptor());
        return factoryBean.getObject();
    }

    private Resource[] resolveMapperLocations() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> mapperResources = new ArrayList<>();
        Collections.addAll(mapperResources, resolver.getResources("classpath*:/mybatis/mapper/*.xml"));
        return mapperResources.toArray(new Resource[0]);
    }

    @Bean
    public PaginationInnerInterceptor pageInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setMaxLimit(-1L);
        paginationInterceptor.setDbType(DbType.MYSQL);
        paginationInterceptor.setOptimizeJoin(true);
        return paginationInterceptor;
    }

    @Bean
    public MybatisPlusInterceptor mybatisInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.setInterceptors(Collections.singletonList(pageInterceptor()));
        return mybatisPlusInterceptor;
    }
}
