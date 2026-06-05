package com.clougence.clouddm.ds.rules.special.tidb;

import java.io.IOException;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.clouddm.ds.tidb.analysis.TiSecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class TiRuleNameNotInKeywordsTest extends MyAbstractRuleTest {

    private final String scriptResource = "rule-test/special/tidb/ti-name-not-in-keywords.txt";

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new TiSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.TiDB;
    }

    @Test
    public void columnNameNotInKeywords_1() throws IOException {
        String sql = null;

        sql = "create table abc_test(id int)";
        assert runScript(scriptResource, sql, null);
        sql = "alter table test.abc change `table` table1 varchar(25);";
        assert runScript(scriptResource, sql, null);
        sql = "create table abc_test(`table` int)";
        assert !runScript(scriptResource, sql, null);
        sql = "alter table test.abc add `elseif` varchar(25);";
        assert !runScript(scriptResource, sql, null);
        sql = "alter table test.abc change `table` `elseif` varchar(25);";
        assert !runScript(scriptResource, sql, null);
        sql = "alter table test.abc change `table` `LAST_VALUE` varchar(25);";
        assert !runScript(scriptResource, sql, null); // LAST_VALUE is keywords
    }

    @Test
    public void tableNameNotInKeywords_1() throws IOException {
        String sql = null;

        sql = "create table `table`(id int(4) primary key auto_increment)";
        assert !runScript(scriptResource, sql, null);
        sql = "create table abc_test(id int(4) primary key auto_increment)";
        assert runScript(scriptResource, sql, null);
        sql = "rename table `table` to abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "rename table `table` to `elseif`";
        assert !runScript(scriptResource, sql, null);
        sql = "rename table abc_test to `elseif`";
        assert !runScript(scriptResource, sql, null);
        sql = "create table `LAST_VALUE`(aa int(4) primary key auto_increment)";
        assert !runScript(scriptResource, sql, null); // LAST_VALUE is keywords
        sql = "alter table test.abc comment 'abc' engine MyISAM, rename `int`";
        assert !runScript(scriptResource, sql, null);
    }
}
