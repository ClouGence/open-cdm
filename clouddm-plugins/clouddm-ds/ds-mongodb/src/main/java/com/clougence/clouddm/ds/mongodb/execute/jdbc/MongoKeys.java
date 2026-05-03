package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import com.clougence.drivers.adapter.JdbcDriver;

public class MongoKeys {

    //
    public static final String ADAPTER_NAME        = JdbcDriver.P_ADAPTER_NAME;
    public static final String ADAPTER_NAME_VALUE  = "mongodb";
    public static final String START_URL           = JdbcDriver.START_URL + ADAPTER_NAME_VALUE + ":";
    public static final String DEFAULT_CLIENT_NAME = "MongoDB-JDBC-Client";

    // for call
    public static final String INTERCEPTOR         = "interceptor";
    // for client
    public static final String SERVER              = JdbcDriver.P_SERVER;
    public static final String TIME_ZONE           = JdbcDriver.P_TIME_ZONE;
    public static final String CONN_TIMEOUT        = "connectTimeout";
    public static final String SO_TIMEOUT          = "socketTimeout";
    public static final String USERNAME            = "username";
    public static final String PASSWORD            = "password";
    public static final String DATABASE            = "database";
    public static final String CLIENT_NAME         = "clientName";
    // for pool
    public static final String MAX_TOTAL           = "maxTotal";
    public static final String MAX_IDLE            = "maxIdle";
    public static final String MIN_IDLE            = "minIdle";
    public static final String TEST_WHILE_IDLE     = "testWhileIdle";
}