package com.clougence.clouddm.ds.sensitive;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.ds.rules.AbstractRuleTest;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sec.rules.domain.CheckerDomain;
import com.clougence.clouddm.sec.rules.domain.special.rdb.RdbValueDomain;
import com.clougence.clouddm.sec.rules.execute.DomainHelper;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.CollectionUtils;

public class SensitiveRuleTestCase extends AbstractRuleTest {

    @Override
    protected SecDomainResolveSpi createSPI() {
        return new MySecDomainResolveSpi(null);
    }

    @Override
    protected DataSourceType currentDsType() {
        return DataSourceType.MySQL;
    }

    private RdbValueDomain createRow(String colName, String colValue) {
        RdbValueDomain domain = new RdbValueDomain();
        domain.setSqlType(SecQueryType.SELECT);
        domain.setAuditKind(SecQueryType.SELECT.getAuditKind());
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog("");
        domain.setSchema("");
        domain.setTable("");
        domain.setColumn(colName);
        domain.setDbType("text");
        domain.setIndex(1);

        domain.setValue(colValue);
        //        domain.setCut(false);

        domain.setEnvId(1);
        domain.setEnvName("test env");
        domain.setDsId(1);
        domain.setDsName("test ds");
        domain.setDsType(DataSourceType.MySQL);
        domain.setUserName("zyc");
        domain.setUserRole("Admin");

        return domain;
    }

    @Test
    public void matchValueSen_1() throws IOException {
        Map<String, String> params1 = CollectionUtils.asMap("match", "abc,cba");
        Map<String, String> params2 = CollectionUtils.asMap("match", "cba");
        Object obj;
        CheckerDomain domain;

        domain = DomainHelper.create(createRow("name", "abc"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-value-sen.txt"), domain, params1);
        assert obj.equals("algorithm::FULL_MASK");
        domain = DomainHelper.create(createRow("name", "cba"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-value-sen.txt"), domain, params1);
        assert obj.equals("algorithm::FULL_MASK");

        domain = DomainHelper.create(createRow("name", "aaa"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-value-sen.txt"), domain, params2);
        assert obj.equals("algorithm::ORIGINAL");

        domain = DomainHelper.create(createRow("name", "aaa"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-value-sen.txt"), domain, null);
        assert obj.equals("algorithm::ORIGINAL");
    }

    @Test
    public void matchColumnSen_1() throws IOException {
        Map<String, String> params1 = CollectionUtils.asMap("match", "name,id");
        Map<String, String> params2 = CollectionUtils.asMap("match", "phone");
        Object obj;
        CheckerDomain domain;

        domain = DomainHelper.create(createRow("name", "abc"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-column-sen.txt"), domain, params1);
        assert obj.equals("algorithm::FULL_MASK");
        domain = DomainHelper.create(createRow("id", "cba"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-column-sen.txt"), domain, params1);
        assert obj.equals("algorithm::FULL_MASK");

        domain = DomainHelper.create(createRow("name", "aaa"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-column-sen.txt"), domain, params2);
        assert obj.equals("algorithm::ORIGINAL");

        domain = DomainHelper.create(createRow("name", "aaa"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-column-sen.txt"), domain, null);
        assert obj.equals("algorithm::ORIGINAL");
    }

    @Test
    public void matchChinaPhoneSen_1() throws IOException {
        Object obj;
        CheckerDomain domain;

        domain = DomainHelper.create(createRow("name", "18651469809"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-china-phone-sen.txt"), domain, null);
        assert obj.equals("algorithm::FULL_MASK");

        domain = DomainHelper.create(createRow("name", "+8618651469809"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-china-phone-sen.txt"), domain, null);
        assert obj.equals("algorithm::ORIGINAL");

        domain = DomainHelper.create(createRow("id", "cba"));
        obj = AbstractRuleTest.runScript(AbstractRuleTest.fromResource("sensitive-test/match-china-phone-sen.txt"), domain, null);
        assert obj.equals("algorithm::ORIGINAL");
    }
}
