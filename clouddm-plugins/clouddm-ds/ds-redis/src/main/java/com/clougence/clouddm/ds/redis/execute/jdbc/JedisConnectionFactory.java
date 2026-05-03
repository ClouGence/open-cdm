package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.lang.reflect.InvocationHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.clougence.drivers.adapter.AdapterFactory;
import com.clougence.drivers.adapter.AdapterTypeSupport;
import com.clougence.drivers.adapter.TypeSupport;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

import redis.clients.jedis.*;

public class JedisConnectionFactory implements AdapterFactory {

    private static HostAndPort passerIpPort(String host, int defaultPort) {
        String[] ipPort = host.split(":");
        if (ipPort.length == 1) {
            return new HostAndPort(ipPort[0], defaultPort);
        } else if (ipPort.length == 2) {
            return new HostAndPort(ipPort[0], Integer.parseInt(ipPort[1]));
        } else {
            throw new IllegalArgumentException("unsupported host format:" + host);
        }
    }

    private static DefaultJedisClientConfig passerClientConfig(Map<String, String> dsConfig) {
        String username = dsConfig.get(JedisKeys.USERNAME);
        String password = dsConfig.get(JedisKeys.PASSWORD);
        String clientName = dsConfig.get(JedisKeys.CLIENT_NAME);
        String defaultDataBase = dsConfig.get(JedisKeys.DATABASE);
        String connTimeoutMsStr = dsConfig.get(JedisKeys.CONN_TIMEOUT);
        String soTimeoutSecStr = dsConfig.get(JedisKeys.SO_TIMEOUT);
        String useTLSStr = "false";//dsConfig.getProperty(JedisKeys.SSL.getConfigKey());

        //
        username = "".equals(username) ? null : username;
        password = "".equals(password) ? null : password;
        clientName = StringUtils.isBlank(clientName) ? JedisKeys.DEFAULT_CLIENT_NAME : clientName;
        int database = StringUtils.isNotBlank(defaultDataBase) ? Integer.parseInt(defaultDataBase) : Protocol.DEFAULT_DATABASE;
        int connTimeoutMs = StringUtils.isBlank(connTimeoutMsStr) ? 5000 : Integer.parseInt(connTimeoutMsStr);
        int soTimeoutSec = (StringUtils.isBlank(soTimeoutSecStr) ? 10 : Integer.parseInt(soTimeoutSecStr)) * 1000;
        boolean useTLS = !StringUtils.isBlank(useTLSStr) && Boolean.parseBoolean(useTLSStr);

        DefaultJedisClientConfig.Builder builder = DefaultJedisClientConfig.builder()
            .connectionTimeoutMillis(connTimeoutMs)
            .socketTimeoutMillis(soTimeoutSec)
            .user(username)
            .password(password)
            .database(database)
            .clientName(clientName);

        if (useTLS) {
            //builder.ssl(true);
            //builder.sslSocketFactory(sslFactory(dsConfig));
            //builder.sslParameters()
            throw new UnsupportedOperationException();
        } else {
            builder.ssl(false);
        }

        return builder.build();
    }

    private static ConnectionPoolConfig passerPoolConfig(Map<String, String> dsConfig) {
        String maxTotalStr = dsConfig.get(JedisKeys.MAX_TOTAL);
        String maxIdleStr = dsConfig.get(JedisKeys.MAX_IDLE);
        String minIdleStr = dsConfig.get(JedisKeys.MIN_IDLE);
        String testWhileIdleStr = dsConfig.get(JedisKeys.TEST_WHILE_IDLE);

        int maxTotal = StringUtils.isBlank(maxTotalStr) ? GenericObjectPoolConfig.DEFAULT_MAX_TOTAL : Integer.parseInt(maxTotalStr);
        int maxIdle = StringUtils.isBlank(maxIdleStr) ? GenericObjectPoolConfig.DEFAULT_MAX_IDLE : Integer.parseInt(maxIdleStr);
        int minIdle = StringUtils.isBlank(minIdleStr) ? GenericObjectPoolConfig.DEFAULT_MIN_IDLE : Integer.parseInt(minIdleStr);
        boolean testWhileIdle = StringUtils.isBlank(minIdleStr) ? GenericObjectPoolConfig.DEFAULT_TEST_WHILE_IDLE : Boolean.parseBoolean(testWhileIdleStr);

        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestWhileIdle(testWhileIdle);
        return poolConfig;
    }

    @Override
    public String getAdapterName() { return JedisKeys.ADAPTER_NAME_VALUE; }

    @Override
    public String[] getPropertyNames() {
        return new String[] { JedisKeys.SERVER, JedisKeys.ADAPTER_NAME, JedisKeys.INTERCEPTOR, JedisKeys.TIME_ZONE, JedisKeys.CONN_TIMEOUT, JedisKeys.SO_TIMEOUT,
                              JedisKeys.USERNAME, JedisKeys.PASSWORD, JedisKeys.DATABASE, JedisKeys.CLIENT_NAME, JedisKeys.MAX_TOTAL, JedisKeys.MAX_IDLE, JedisKeys.MIN_IDLE,
                              JedisKeys.TEST_WHILE_IDLE };
    }

    @Override
    public TypeSupport createTypeSupport(Properties properties) {
        return new AdapterTypeSupport(properties);
    }

    //    private static SSLSocketFactory sslFactory(Properties dsConfig) throws Exception {
    //        String caFile = dsConfig.getProperty(DsConfigKeys.SSL_CA_FILE.getConfigKey());
    //        String clientCertFile = dsConfig.getProperty(DsConfigKeys.SSL_CLIENT_CERT_FILE.getConfigKey());
    //        String clientKeyFile = dsConfig.getProperty(DsConfigKeys.SSL_CLIENT_KEY_FILE.getConfigKey());
    //        String clientKeyPassword = dsConfig.getProperty(DsConfigKeys.SSL_CLIENT_KEY_PASSWORD.getConfigKey());
    //        String mode = dsConfig.getProperty(DsConfigKeys.SSL_MODE.getConfigKey());
    //
    //        String certName;
    //        byte[] certBytes;
    //        if (certBytes == null || certBytes.length == 0) {
    //             Jedis use null as default
    //            return null;
    //        }
    //
    //        CertificateFactory cf = CertificateFactory.getInstance("X.509");
    //        Certificate ca = cf.generateCertificate(new ByteArrayInputStream(certBytes));
    //
    //        String keyStoreType = KeyStore.getDefaultType();
    //        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    //        keyStore.load(null, null); // init an empty KeyStore
    //        keyStore.setCertificateEntry("ca", ca);
    //
    //        // TrustManagerFactory use to manage the trust material
    //        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    //        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    //        tmf.init(keyStore);
    //
    //        SSLContext sslContext = SSLContext.getInstance("TLS");
    //        sslContext.init(null, tmf.getTrustManagers(), null);
    //        return sslContext.getSocketFactory();
    //    }

    @Override
    public JedisConnection createConnection(Connection owner, String jdbcUrl, Properties props) throws SQLException {
        Map<String, String> caseProps = new LinkedCaseInsensitiveMap<>();
        props.forEach((k, v) -> {
            caseProps.put((String) k, v == null ? "" : String.valueOf(v));
        });

        String host = caseProps.get(JedisKeys.SERVER);
        String customJedis = caseProps.get(JedisKeys.CUSTOM_JEDIS);
        String defaultDataBase = caseProps.get(JedisKeys.DATABASE);
        Object jedisObject;
        int database;

        if (StringUtils.isNotBlank(customJedis)) {
            try {
                Class<?> customJedisClass = JedisConnectionFactory.class.getClassLoader().loadClass(customJedis);
                CustomJedis customJedisCmd = (CustomJedis) customJedisClass.newInstance();
                jedisObject = customJedisCmd.createJedisCmd(jdbcUrl, caseProps);
                database = StringUtils.isNotBlank(defaultDataBase) ? Integer.parseInt(defaultDataBase) : Protocol.DEFAULT_DATABASE;
                if (jedisObject == null) {
                    throw new SQLException("create jedis connection failed, custom jedis return null.");
                }
            } catch (Exception e) {
                throw new SQLException(e);
            }
        } else if (host.contains(";")) {
            Set<HostAndPort> clusterHosts = new HashSet<>();
            for (String h : StringUtils.split(host, ';')) {
                clusterHosts.add(passerIpPort(h, 6379));
            }

            DefaultJedisClientConfig clientConfig = passerClientConfig(caseProps);
            int maxAttempts = 5;
            Duration maxTotalRetriesDuration = Duration.ofMillis((long) maxAttempts * clientConfig.getSocketTimeoutMillis());
            ConnectionPoolConfig poolConfig = passerPoolConfig(caseProps);
            jedisObject = new JedisCluster(clusterHosts, clientConfig, maxAttempts, maxTotalRetriesDuration, poolConfig);
            database = clientConfig.getDatabase();
        } else {
            HostAndPort hostAndPort = passerIpPort(host, 6379);
            DefaultJedisClientConfig clientConfig = passerClientConfig(caseProps);
            jedisObject = new Jedis(hostAndPort, clientConfig);
            database = clientConfig.getDatabase();
        }

        if (jedisObject instanceof JedisCluster) {
            JedisCmd cmd = new JedisCmd((JedisCluster) jedisObject, this.createInvocation(caseProps));
            return new JedisConnection(owner, cmd, jdbcUrl, caseProps, database);
        } else if (jedisObject instanceof Jedis) {
            JedisCmd cmd = new JedisCmd((Jedis) jedisObject, this.createInvocation(caseProps));
            return new JedisConnection(owner, cmd, jdbcUrl, caseProps, database);
        } else {
            throw new SQLException("create jedis connection failed, unknown jedis object type " + jedisObject.getClass().getName());
        }
    }

    private InvocationHandler createInvocation(Map<String, String> props) throws SQLException {
        if (props.containsKey(JedisKeys.INTERCEPTOR)) {
            try {
                String interceptorClass = props.get(JedisKeys.INTERCEPTOR);
                Class<?> interceptor = ClassUtils.getClass(JedisConnectionFactory.class.getClassLoader(), interceptorClass);
                return (InvocationHandler) interceptor.newInstance();
            } catch (Exception e) {
                throw new SQLException("create interceptor failed, " + e.getMessage(), e);
            }
        } else {
            return null;
        }
    }
}
