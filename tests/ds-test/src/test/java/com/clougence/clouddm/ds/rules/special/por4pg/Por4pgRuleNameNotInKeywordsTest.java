package com.clougence.clouddm.ds.rules.special.por4pg;

import java.io.IOException;

import org.junit.Test;

import com.clougence.clouddm.ds.polardb.analysis.porpg.PorPgSecDomainResolveSpi;
import com.clougence.clouddm.ds.rules.rdb.using_my.MyAbstractRuleTest;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Por4pgRuleNameNotInKeywordsTest extends MyAbstractRuleTest {

    private final String scriptResource = "rule-test/special/por4pg/por4pg-name-not-in-keywords.txt";

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new PorPgSecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.PolarDBPg;
    }

    @Test
    public void catalogNotInKeywords_1() throws IOException {
        String sql = null;

        sql = "create database \"table\"";
        assert !runScript(scriptResource, sql, null);
        sql = "create database abc_test";
        assert runScript(scriptResource, sql, null);

        sql = "alter database abc rename to \"table\"";
        assert !runScript(scriptResource, sql, null);
        sql = "alter database abc rename to abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "alter database \"table\" rename to abc_test";
        assert runScript(scriptResource, sql, null);
    }

    @Test
    public void schemaNotInKeywords_1() throws IOException {
        String sql = null;

        //sql = "create schema \"table\"";
        //assert !runScript(scriptResource, sql, null);
//        sql = "create schema \"table\"";
//        assert !runScript(scriptResource, sql, null);

        sql = "create schema abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "create schema abc_test";
        assert runScript(scriptResource, sql, null);
        //sql = "create schema \"table\".abc_test";
        //assert runScript(scriptResource, sql, null);
    }

    @Test
    public void schemaNotInKeywords_2() throws IOException {
        String sql = null;

        sql = "alter schema abc rename to abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "alter schema test.abc rename to abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "alter schema test.\"table\" rename to abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "alter schema \"table\".\"table\" rename to abc_test";
        assert runScript(scriptResource, sql, null);
    }

    @Test
    public void schemaNotInKeywords_3() throws IOException {
        String sql = null;

        sql = "alter schema abc rename to \"else\"";
        assert !runScript(scriptResource, sql, null);
        sql = "alter schema test.abc rename to \"else\"";
        assert !runScript(scriptResource, sql, null);
        sql = "alter schema test.\"table\" rename to \"else\"";
        assert !runScript(scriptResource, sql, null);
        sql = "alter schema \"table\".\"table\" rename to \"else\"";
        assert !runScript(scriptResource, sql, null);
    }

    @Test
    public void tableNotInKeywords_1() throws IOException {
        String sql = null;

        sql = "create table \"table\"(id int primary key, name varchar(25) not null)";
        assert !runScript(scriptResource, sql, null);
        sql = "create table abc_test(id int primary key, name varchar(25) not null)";
        assert runScript(scriptResource, sql, null);

        sql = "alter table \"table\" rename to abc_test";
        assert runScript(scriptResource, sql, null);
        sql = "alter table \"table\" rename to \"else\"";
        assert !runScript(scriptResource, sql, null);
    }

    @Test
    public void columnNameNotInKeywords_1() throws IOException {
        String sql = null;

        sql = "create table abc_test(id int)";
        assert runScript(scriptResource, sql, null);
        sql = "create table abc_test(id int, \"table\" int)";
        assert !runScript(scriptResource, sql, null);

        sql = "alter table abc_test add name varchar(25);";
        assert runScript(scriptResource, sql, null);
        //sql = "alter table abc_test add \"else\" varchar(25);";
        //assert !runScript(scriptResource, sql, null);

        sql = "alter table test rename column a1 to a123456;";
        assert runScript(scriptResource, sql, null);
        sql = "alter table test rename column \"table\" to a123456;";
        assert runScript(scriptResource, sql, null);
        sql = "alter table test rename column a1 to \"table\";";
        assert !runScript(scriptResource, sql, null);
    }
}
