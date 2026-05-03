package com.clougence.clouddm.ds.rules.special.mariadb;

import java.io.IOException;

import org.junit.Test;

import com.clougence.clouddm.ds.mariadb.analysis.MarSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class MarRuleNameNotInKeywordsTest extends MyAbstractRuleTest {

    private final String scriptResource = "rule-test/special/mariadb/mar-name-not-in-keywords.txt";

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MarSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MariaDB;
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
        assert runScript(scriptResource, sql, null);
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
        assert runScript(scriptResource, sql, null);
        sql = "alter table test.abc comment 'abc' engine MyISAM, rename `int`";
        assert !runScript(scriptResource, sql, null);
    }
}
