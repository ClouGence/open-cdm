///*
// * Copyright 2015-2022 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.clougence.faker.realdb;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import com.alibaba.druid.pool.DruidDataSource;
//
///***
// * 创建 JDBC
// * @version : 2014-1-13
// * @author 赵永春 (zyc@hasor.net)
// */
//public class DsUtils {
//
//    //    public static String MYSQL_JDBC_URL  = "jdbc:mysql://192.168.0.152:50201/pudding_test?allowMultiQueries=true";
//    //    public static String MYSQL_JDBC_URL  = "jdbc:mysql://127.0.0.1:3306/dingtax?allowMultiQueries=true";
//    //    public static String MYSQL_JDBC_URL  = "jdbc:mysql://120.55.58.29:2881/dingtax?allowMultiQueries=true";
//    //    public static String MYSQL_JDBC_URL  = "jdbc:mysql://obmtgl4qmkj0ncv0-mi.oceanbase.aliyuncs.com:3306/ob_full_test?allowMultiQueries=true";
//    //    public static String MYSQL_JDBC_URL = "jdbc:mysql://121.41.224.11:2881/huasheng?allowMultiQueries=true";
//    //    public static String PG_JDBC_URL = "jdbc:postgresql://pgm-bp15e1r0dl2a86w1ao.pg.rds.aliyuncs.com:5432/dm_test";
//    //    public static String ORACLE_JDBC_URL = "jdbc:oracle:thin:@192.168.0.148:1521:orcl";
//    //    public static String ORACLE_JDBC_URL = "jdbc:oracle:thin:@192.168.0.192:1521/ORCL";
//    //    public static String ORACLE_JDBC_URL = "jdbc:oracle:thin:@121.40.188.77:1521/ORCL";
//    public static String MSSQL_JDBC_URL = "jdbc:sqlserver://192.168.0.148:1433;databaseName=console;trustServerCertificate=true;sendTimeAsDateTime=false";
//    public static String MYSQL_JDBC_URL = "jdbc:mysql://127.0.0.1:3306/olddream?allowMultiQueries=true";
//    //    public static String PG_JDBC_URL     = "jdbc:postgresql://localhost:5432/postgres";
//    public static String PG_JDBC_URL = "jdbc:postgresql://192.168.0.152:50401/console";
//    public static String ORACLE_JDBC_URL = "jdbc:oracle:thin:@192.168.0.148:1521:orcl";
//    public static String MSSQL_JDBC_URL1 = "jdbc:sqlserver://112.124.32.137:1433;databaseName=console;trustServerCertificate=true;sendTimeAsDateTime=false";
//    public static String MSSQL_JDBC_URL2 = "jdbc:jtds:sqlserver://121.40.64.156:1433/console";
//    public static String DM_JDBC_URL = "jdbc:dm://127.0.0.1:5236";
//    public static String HANA_JDBC_URL = "jdbc:sap://192.168.0.129:39013?databaseName=HXE";
//    //public static String HANA_JDBC_URL     = "jdbc:sap://20.189.124.3:39015?databaseName=HXE";
//    public static String RSFT_JDBC_URL = "jdbc:redshift://clougence-workgroup.367220705952.ap-southeast-1.redshift-serverless.amazonaws.com:5439/dev";
//
//    private static DruidDataSource createDs(String driver, String url, String user, String password) throws SQLException {
//        DruidDataSource druid = new DruidDataSource();
//        druid.setUrl(url);
//        druid.setDriverClassName(driver);
//        druid.setUsername(user);
//        druid.setPassword(password);
//        druid.setMaxActive(40);
//        druid.setMaxWait(30000);
//        druid.setInitialSize(1);
//        druid.setConnectionErrorRetryAttempts(1);
//        druid.setBreakAfterAcquireFailure(true);
//        druid.setTestOnBorrow(true);
//        druid.setTestWhileIdle(true);
//        druid.setFailFast(true);
//        druid.init();
//        return druid;
//    }
//
//    public static Connection localMySql() throws SQLException {
//        return DriverManager.getConnection(MYSQL_JDBC_URL, "root", "123456");
//    }
//
//    public static Connection localPg() throws SQLException {
//        return DriverManager.getConnection(PG_JDBC_URL, "postgres", "123456");
//    }
//
//    public static Connection localOracle() throws SQLException {
//        Connection connection = DriverManager.getConnection(ORACLE_JDBC_URL, "sys as sysdba", "Oracle2021");
//        connection.createStatement().execute("alter session set current_schema = SCOTT");
//        return connection;
//    }
//
//    public static DruidDataSource dsMySql() throws SQLException {
//        return createDs("com.mysql.jdbc.Driver", MYSQL_JDBC_URL, "root", "caishan2001");
//        //        return createDs("com.mysql.jdbc.Driver", MYSQL_JDBC_URL, "origin", "123258");
//        //        return createDs("com.mysql.jdbc.Driver", MYSQL_JDBC_URL, "root", "74123963");
//        //        return createDs("com.mysql.jdbc.Driver", MYSQL_JDBC_URL, "clougence_abc", "clougence_ABC");
//        //        return createDs("com.mysql.jdbc.Driver", MYSQL_JDBC_URL, "obtest", "obtest");
//    }
//
//    public static DruidDataSource dsPg() throws SQLException {
//        return createDs("org.postgresql.Driver", PG_JDBC_URL, "postgres", "123456");
//    }
//
//    public static DruidDataSource dsOracle() throws SQLException {
//        //        return createDs("oracle.jdbc.driver.OracleDriver", ORACLE_JDBC_URL, "sys as sysdba", "Oracle2021");
//        return createDs("oracle.jdbc.OracleDriver", ORACLE_JDBC_URL, "junyu_orcl", "74123963");
//    }
//
//    public static DruidDataSource dsSqlServer() throws SQLException {
//        return createDs("com.microsoft.sqlserver.jdbc.SQLServerDriver", MSSQL_JDBC_URL1, "sa", "CloudCanal@123");
//    }
//
//    public static DruidDataSource dsSqlServerJTDS() throws SQLException {
//        return createDs("net.sourceforge.jtds.jdbc.Driver", MSSQL_JDBC_URL2, "sa", "CloudCanal@123");
//    }
//
//    public static DruidDataSource dsRedshift() throws SQLException {
//        return createDs("com.amazon.redshift.Driver", RSFT_JDBC_URL, "admin", "Qwe123456");
//    }
//
//    public static DruidDataSource dsDm8() throws SQLException {
//        return createDs("dm.jdbc.driver.DmDriver", DM_JDBC_URL, "SYSDBA", "SYSDBA001");
//    }
//
//    public static DruidDataSource dsHana() throws SQLException {
//        //return createDs("com.sap.db.jdbc.Driver", HANA_JDBC_URL, "SYSTEM", "Hanaexpress@123");
//        return createDs("com.sap.db.jdbc.Driver", HANA_JDBC_URL, "SYSTEM", "Clougence2022");
//    }
//}
