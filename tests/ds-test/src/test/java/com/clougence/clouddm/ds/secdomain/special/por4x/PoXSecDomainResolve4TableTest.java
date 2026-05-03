package com.clougence.clouddm.ds.secdomain.special.por4x;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.polardb.analysis.porx.PorXResAnalysisSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSecDomainResolveSpi;
import com.clougence.clouddm.ds.polardb.analysis.porx.PorXSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.mysql.MySecDomainTestSupport;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyIndexDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PoXSecDomainResolve4TableTest extends MySecDomainTestSupport {

    public PoXSecDomainResolve4TableTest(){
        this.analysisSpi = new PorXResAnalysisSpi(null);
        this.resolveSpi = new PorXSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new PorXSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.PolarDbX;
    }

    @Test
    public void createTable_1() {
        String sql = "CREATE TABLE single_tbl( id bigint not null auto_increment,  name varchar(30), primary key(id));";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/single_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/single_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/single_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("single_tbl") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createTable_2() {
        String sql = "CREATE TABLE tb1 (id int) LOCALITY='dn=polardbx-storage-0-master';";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/tb1/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/tb1/id/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 2;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("tb1") && domain.getOptions().isEmpty();
        }
    }

    @Test
    public void createTable_3() {
        String sql = "CREATE TABLE multi_db_single_tbl(  id bigint not null auto_increment,   name varchar(30),   primary key(id)) dbpartition by hash(id);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/multi_db_single_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/multi_db_single_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/multi_db_single_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("multi_db_single_tbl") && domain.getOptions().isEmpty();
            assert domain.getColumns().equals(Arrays.asList("id", "name"));
        }
    }

    @Test
    public void createTableHash_1() {
        String sql = "CREATE TABLE multi_db_multi_tbl( id bigint not null auto_increment,  bid int,  name varchar(30),  primary key(id)) dbpartition by hash(id) tbpartition by hash(bid) tbpartitions 3;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 4;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/bid/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("multi_db_multi_tbl") && domain.getOptions().isEmpty();
            assert domain.getColumns().equals(Arrays.asList("id", "bid", "name"));
            assert domain.isHasPrimary();
        }
    }

    @Test
    public void createTableHash_2() {
        String sql = "create table test_order_tb ( id bigint not null auto_increment, seller_id varchar(30) DEFAULT NULL, order_id varchar(30) DEFAULT NULL, buyer_id varchar(30) DEFAULT NULL, create_time datetime DEFAULT NULL,primary key(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8 dbpartition by RANGE_HASH(buyer_id, order_id, 10) tbpartition by RANGE_HASH(buyer_id, order_id, 10) tbpartitions 3;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 6;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/test_order_tb/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/test_order_tb/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/test_order_tb/seller_id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/test_order_tb/order_id/");
            assert resList.get(4).getType() == TargetType.Column &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_schema/test_order_tb/buyer_id/");
            assert resList.get(5).getType() == TargetType.Column &&//
                   resList.get(5).toDsResPath().getResPath().equals("/test_schema/test_order_tb/create_time/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 7;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("test_order_tb") && domain.getEngine().equals("InnoDB");
            assert domain.getColumns().equals(Arrays.asList("id", "seller_id", "order_id", "buyer_id", "create_time"));
            assert domain.isHasPrimary();
            assert domain.getCharacterSet().equals("utf8");
        }
    }

    @Test
    public void createTableHash_3() {
        String sql = "CREATE TABLE user_log( userId int,  name varchar(30),  operation varchar(30),  actionDate DATE) dbpartition by hash(userId) tbpartition by WEEK(actionDate) tbpartitions 7;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 5;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_log/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/user_log/userId/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/user_log/name/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/user_log/operation/");
            assert resList.get(4).getType() == TargetType.Column &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_schema/user_log/actionDate/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 5;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_log") && domain.getOptions().isEmpty();
            assert domain.getColumns().equals(Arrays.asList("userId", "name", "operation", "actionDate"));
            assert !domain.isHasPrimary();
        }
    }

    @Test
    public void createTableHash_4() {
        String sql = "CREATE TABLE multi_db_single_tbl( id bigint not null auto_increment, name varchar(30),   primary key(id)) dbpartition by hash(id);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/multi_db_single_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/multi_db_single_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/multi_db_single_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("multi_db_single_tbl") && domain.getOptions().isEmpty();
            assert domain.getColumns().equals(Arrays.asList("id", "name"));
        }
    }

    @Test
    public void createPriKeyTbl_1() {
        String sql = "CREATE TABLE prmkey_tbl( id bigint not null auto_increment,  name varchar(30),  primary key(id)) dbpartition by hash();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/prmkey_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/prmkey_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/prmkey_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("prmkey_tbl") && domain.getOptions().isEmpty();
            assert domain.getColumns().equals(Arrays.asList("id", "name"));
            assert domain.isHasPrimary();
        }
    }

    @Test
    public void createPriKeyTbl_2() {
        String sql = "CREATE TABLE prmkey_multi_tbl( id bigint not null auto_increment, name varchar(30), primary key(id)) dbpartition by hash() tbpartition by hash() tbpartitions 3;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/prmkey_multi_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/prmkey_multi_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/prmkey_multi_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("prmkey_multi_tbl") && domain.getOptions().isEmpty();
            assert domain.getColumns().equals(Arrays.asList("id", "name"));
            assert domain.isHasPrimary();
        }
    }

    @Test
    public void createTableBroadcast_1() {
        String sql = "CREATE TABLE brd_tbl( id bigint not null auto_increment,  name varchar(30),  primary key(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8 BROADCAST;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/brd_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/brd_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/brd_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("brd_tbl") && domain.getEngine().equals("InnoDB");
            assert domain.getColumns().equals(Arrays.asList("id", "name"));
            assert domain.isHasPrimary();
            assert domain.getCharacterSet().equals("utf8");
        }
    }

    @Test
    public void createTableBroadcast_2() {
        String sql = "CREATE TABLE multi_db_multi_tbl( id bigint not null auto_increment, name varchar(30), primary key(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8 dbpartition by hash(id) tbpartition by hash(id) tbpartitions 3;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 3;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/multi_db_multi_tbl/name/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 4;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("multi_db_multi_tbl") && domain.getEngine().equals("InnoDB");
            assert domain.getColumns().equals(Arrays.asList("id", "name"));
            assert domain.isHasPrimary();
            assert domain.getCharacterSet().equals("utf8");
        }
    }

    @Test
    public void createTableIndex_1() {
        String sql = "CREATE TABLE t_order (\n" + " `id` bigint(11) NOT NULL AUTO_INCREMENT,\n" + " `order_id` varchar(20) DEFAULT NULL,\n"
                     + " `buyer_id` varchar(20) DEFAULT NULL,\n" + " `seller_id` varchar(20) DEFAULT NULL,\n" + " `order_snapshot` longtext DEFAULT NULL,\n"
                     + " `order_detail` longtext DEFAULT NULL,\n" + " PRIMARY KEY (`id`),\n" + " GLOBAL INDEX `g_i_seller`(`seller_id`) dbpartition by hash(`seller_id`)\n"
                     + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 dbpartition by hash(`order_id`);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 7;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/t_order/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/t_order/order_id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/t_order/buyer_id/");
            assert resList.get(4).getType() == TargetType.Column &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_schema/t_order/seller_id/");
            assert resList.get(5).getType() == TargetType.Column &&//
                   resList.get(5).toDsResPath().getResPath().equals("/test_schema/t_order/order_snapshot/");
            assert resList.get(6).getType() == TargetType.Column &&//
                   resList.get(6).toDsResPath().getResPath().equals("/test_schema/t_order/order_detail/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 9;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("t_order") && domain.getEngine().equals("InnoDB");
            assert domain.getColumns().equals(Arrays.asList("id", "order_id", "buyer_id", "seller_id", "order_snapshot", "order_detail"));
            assert domain.isHasPrimary();
            assert domain.getCharacterSet().equals("utf8");
        }
        {
            RdbIndexDomain rdbIndexDomain = (RdbIndexDomain) domainList.get(8);
            assert rdbIndexDomain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_INDEX && rdbIndexDomain.getAuditKind() == SecQueryKind.CREATE;
            assert rdbIndexDomain.getCatalog() == null && rdbIndexDomain.getSchema() == null && rdbIndexDomain.getTableName().equals("t_order");
            assert rdbIndexDomain.getName().equals("g_i_seller");
            assert rdbIndexDomain.getColumns().equals(Arrays.asList("seller_id"));
            assert rdbIndexDomain.getType().equals("index");
        }
    }

    @Test
    public void createTableIndex_2() {
        String sql = "CREATE TABLE t_order (\n" + " `id` bigint(11) NOT NULL AUTO_INCREMENT,\n" + " `order_id` varchar(20) DEFAULT NULL,\n"
                     + " `buyer_id` varchar(20) DEFAULT NULL,\n" + " `seller_id` varchar(20) DEFAULT NULL,\n" + " `order_snapshot` longtext DEFAULT NULL,\n"
                     + " `order_detail` longtext DEFAULT NULL,\n" + " PRIMARY KEY (`id`),\n"
                     + " UNIQUE GLOBAL INDEX `g_i_buyer`(`buyer_id`) COVERING(`seller_id`, `order_snapshot`) \n"
                     + "   dbpartition by hash(`buyer_id`) tbpartition by hash(`buyer_id`) tbpartitions 3\n"
                     + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 dbpartition by hash(`order_id`);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 7;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/t_order/id/");
            assert resList.get(2).getType() == TargetType.Column &&//
                   resList.get(2).toDsResPath().getResPath().equals("/test_schema/t_order/order_id/");
            assert resList.get(3).getType() == TargetType.Column &&//
                   resList.get(3).toDsResPath().getResPath().equals("/test_schema/t_order/buyer_id/");
            assert resList.get(4).getType() == TargetType.Column &&//
                   resList.get(4).toDsResPath().getResPath().equals("/test_schema/t_order/seller_id/");
            assert resList.get(5).getType() == TargetType.Column &&//
                   resList.get(5).toDsResPath().getResPath().equals("/test_schema/t_order/order_snapshot/");
            assert resList.get(6).getType() == TargetType.Column &&//
                   resList.get(6).toDsResPath().getResPath().equals("/test_schema/t_order/order_detail/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 9;
        {
            MyTableDomain domain = (MyTableDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("t_order") && domain.getEngine().equals("InnoDB");
            assert domain.isHasPrimary();
            assert domain.getCharacterSet().equals("utf8");
        }
        {
            MyIndexDomain rdbConstraintDomain = (MyIndexDomain) domainList.get(8);
            assert rdbConstraintDomain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_INDEX && rdbConstraintDomain.getAuditKind() == SecQueryKind.CREATE;
            assert rdbConstraintDomain.getCatalog() == null && rdbConstraintDomain.getSchema() == null && rdbConstraintDomain.getTableName().equals("t_order");
            assert rdbConstraintDomain.getName().equals("g_i_buyer");
            assert rdbConstraintDomain.getColumns().equals(Arrays.asList("buyer_id"));
            assert rdbConstraintDomain.getType().equals("index");
        }
    }

    @Test
    public void alterTableOnline_1() {
        String sql = "ALTER online TABLE user_log ADD COLUMN idcard varchar(30);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_log/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/user_log/idcard/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbColumnDomain domain = (RdbColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_log");
            assert domain.getTypeName().equals("varchar");
            assert domain.getTypeDesc().equals("varchar(30)");
            assert domain.isNullable();
            assert domain.getColumn().equals("idcard");
        }
    }

    @Test
    public void alterTableOnline_2() {
        String sql = "ALTER OFFLINE TABLE user_log ADD COLUMN idcard varchar(30);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/user_log/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/user_log/idcard/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbColumnDomain domain = (RdbColumnDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("user_log");
            assert domain.getTypeName().equals("varchar");
            assert domain.getTypeDesc().equals("varchar(30)");
            assert domain.isNullable();
            assert domain.getColumn().equals("idcard");
        }
    }

    @Test
    public void alterTableIndex_1() {
        String sql = "ALTER TABLE t_order ADD UNIQUE GLOBAL INDEX `g_i_buyer` (`buyer_id`) COVERING (`order_snapshot`) dbpartition by hash(`buyer_id`);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain domain = (MyIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t_order");
            assert domain.getColumns().equals(Arrays.asList("buyer_id"));
            assert domain.getType().equals("unique");
        }
    }

    @Test
    public void alterTableIndex_2() {
        String sql = "ALTER TABLE t_order ADD CONSTRAINT UNIQUE GLOBAL INDEX `g_i_buyer` (`buyer_id`) COVERING (`order_snapshot`) dbpartition by hash(`buyer_id`);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain domain = (MyIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("t_order");
            assert domain.getColumns().equals(Arrays.asList("buyer_id"));
            assert domain.getType().equals("unique");
        }
    }

    @Test
    public void alterTableIndex_3() {
        String sql = "ALTER TABLE t_order rename index a to b;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain1 = (RdbIndexDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.ALTER_INDEX && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTableName().equals("t_order");
            assert domain1.getColumns() == null;
            assert domain1.getNewName().equals("b");
            assert domain1.getName().equals("a");
        }
    }

    @Test
    public void createIndex_1() {
        String sql = "create unique global index test on table1(a,b) dbpartition by hash();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("table1");
            assert domain.getColumns().equals(Arrays.asList("a", "b"));
            assert domain.getType().equals("unique");
        }
    }

    @Test
    public void createIndex_2() {
        String sql = "create unique global index  test on table1(a,b) dbpartition by hash();";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table1/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.CREATE_INDEX;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            RdbIndexDomain domain = (RdbIndexDomain) domainList.get(0);
            assert domain.getSqlType() == SecQueryType.CREATE_INDEX && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTableName().equals("table1");
            assert domain.getColumns().equals(Arrays.asList("a", "b"));
            assert domain.getType().equals("unique");
        }
    }

    @Test
    public void createIndex_3() {
        String sql = "ALTER TABLE t_order ADD UNIQUE GLOBAL INDEX `g_i_buyer` (`buyer_id`) COVERING (`order_snapshot`) dbpartition by hash(`buyer_id`);";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain constraintDomain = (MyIndexDomain) domainList.get(0);
            assert constraintDomain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_INDEX && constraintDomain.getAuditKind() == SecQueryKind.CREATE;
            assert constraintDomain.getCatalog() == null && constraintDomain.getSchema() == null && constraintDomain.getTableName().equals("t_order");
            assert constraintDomain.getColumns().equals(Arrays.asList("buyer_id"));
            assert constraintDomain.getType().equals("unique");
        }
    }

    @Test
    public void createIndex_4() {
        String sql = "ALTER TABLE t_order ADD UNIQUE GLOBAL INDEX `g_i_buyer` (`buyer_id`) COVERING (`order_snapshot`) dbpartition by hash(`buyer_id`) lock = default;";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/t_order/");
        }

        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 1;
        {
            assert splitScripts.get(0).getScript().equals(sql);
            assert splitScripts.get(0).getType() == SecQueryType.ALTER_TABLE;
        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            MyIndexDomain constraintDomain = (MyIndexDomain) domainList.get(0);
            assert constraintDomain.getSqlType() == SecQueryType.ALTER_TABLE_ADD_INDEX && constraintDomain.getAuditKind() == SecQueryKind.CREATE;
            assert constraintDomain.getCatalog() == null && constraintDomain.getSchema() == null && constraintDomain.getTableName().equals("t_order");
            assert constraintDomain.getColumns().equals(Arrays.asList("buyer_id"));
            assert constraintDomain.getType().equals("unique");
        }
    }
}
