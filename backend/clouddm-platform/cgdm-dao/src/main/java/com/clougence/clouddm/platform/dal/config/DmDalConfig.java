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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.drivers.DataSourceBridge;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;
import com.clougence.utils.loader.CgClassLoader;
import com.clougence.utils.loader.providers.JarResourceLoader;
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
    public static final String   MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String  DRIVER_JAR_RESOURCE     = "driver-jar/mysql-connector-j-8.0.33.jar";
    private static final String  DRIVER_JAR_FILE         = "cgdm-mysq/mysql-connector-j-8.0.33.jar";
    private static CgClassLoader driverClassLoader;

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
        CgClassLoader classLoader = ensureDriverAvailable();
        DmDalConfigDsFactory dsFactory = new DmDalConfigDsFactory(classLoader);

        // Hikari
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.CUSTOM_URL.getConfigKey(), jdbcUrl);
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), username);
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), StringUtils.defaultString(password));
        properties.setProperty(DsConfigKeys.LOGIN_TIMEOUT_MS.getConfigKey(), Long.toString(connectionTimeout));

        return new DataSourceBridge(properties, dsFactory);
    }

    public static synchronized CgClassLoader ensureDriverAvailable() {
        if (driverClassLoader != null) {
            try {
                driverClassLoader.loadClass(MYSQL_DRIVER_CLASS_NAME);
                return driverClassLoader;
            } catch (ClassNotFoundException e) {
                log.debug("Cached runtime MySQL driver class is unavailable: {}", e.getMessage());
                IOUtils.closeQuietly(driverClassLoader);
                driverClassLoader = null;
            }
        }

        CgClassLoader classLoader = createDriverClassLoader(false);
        try {
            classLoader.loadClass(MYSQL_DRIVER_CLASS_NAME);
            driverClassLoader = classLoader;
            return classLoader;
        } catch (ClassNotFoundException e) {
            log.debug("Runtime MySQL driver class is unavailable after loading cache: {}", e.getMessage());
            IOUtils.closeQuietly(classLoader);
        }

        classLoader = createDriverClassLoader(true);
        try {
            classLoader.loadClass(MYSQL_DRIVER_CLASS_NAME);
            driverClassLoader = classLoader;
            return classLoader;
        } catch (ClassNotFoundException e) {
            IOUtils.closeQuietly(classLoader);
            throw new IllegalStateException("Runtime MySQL driver class is unavailable after refreshing cache: " + MYSQL_DRIVER_CLASS_NAME, e);
        }
    }

    private static CgClassLoader createDriverClassLoader(boolean refresh) {
        File driverJarFile = new File(GlobalConfUtils.getTempData(DRIVER_JAR_FILE));
        if (refresh) {
            try {
                Files.deleteIfExists(driverJarFile.toPath());
            } catch (IOException e) {
                throw new IllegalStateException("Delete runtime MySQL driver cache failed: " + driverJarFile.getAbsolutePath(), e);
            }
        }
        if (!driverJarFile.isFile()) {
            copyDriverJarResource(driverJarFile);
        }
        if (!driverJarFile.isFile()) {
            throw new IllegalStateException("Runtime MySQL driver jar is unavailable: " + driverJarFile.getAbsolutePath());
        }
        try {
            return new JarResourceLoader(driverJarFile).toClassLoader(DmDalConfig.class.getClassLoader());
        } catch (IOException e) {
            throw new IllegalStateException("Load runtime MySQL driver jar failed: " + driverJarFile.getAbsolutePath(), e);
        }
    }

    private static void copyDriverJarResource(File driverJarFile) {
        try (InputStream input = DmDalConfig.class.getClassLoader().getResourceAsStream(DRIVER_JAR_RESOURCE)) {
            if (input == null) {
                if (driverJarFile.isFile()) {
                    log.warn("Runtime MySQL driver resource {} not found in classpath, use existing file {}.", DRIVER_JAR_RESOURCE, driverJarFile.getAbsolutePath());
                    return;
                }
                throw new IllegalStateException("Runtime MySQL driver resource is missing: " + DRIVER_JAR_RESOURCE);
            }

            File parentFile = driverJarFile.getParentFile();
            if (parentFile != null) {
                Files.createDirectories(parentFile.toPath());
            }
            Files.copy(input, driverJarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Copy runtime MySQL driver resource failed: " + DRIVER_JAR_RESOURCE + " -> " + driverJarFile.getAbsolutePath(), e);
        }
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
