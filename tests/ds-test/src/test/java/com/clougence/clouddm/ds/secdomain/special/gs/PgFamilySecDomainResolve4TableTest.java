package com.clougence.clouddm.ds.secdomain.special.gs;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.ds.gauss.analysis.gs.GsResAnalysisSpi;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSecDomainResolveSpi;
import com.clougence.clouddm.ds.gauss.analysis.gs.GsSplitAnalysisSpi;
import com.clougence.clouddm.ds.secdomain.family.postgres.PgSecDomainResolve4TableTest;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class PgFamilySecDomainResolve4TableTest extends PgSecDomainResolve4TableTest {

    public PgFamilySecDomainResolve4TableTest(){
        this.analysisSpi = new GsResAnalysisSpi(null);
        this.resolveSpi = new GsSecDomainResolveSpi(null);
        this.splitAnalysisSpi = new GsSplitAnalysisSpi();
        this.dataSourceType = DataSourceType.GaussDB;
    }

    @Test
    public void createTable_partition_1() {
        String sql = "CREATE TABLE abc\n" +                                //
                     "(\n" +                                               //
                     "    id       int                       " +           //
                     ")\n" +                                               //
                     "PARTITION BY RANGE(id)\n" +                          //
                     "(\n" +                                               //
                     "        PARTITION P1 VALUES LESS THAN(2450815),\n" + //
                     "        PARTITION P2 VALUES LESS THAN(2451179),\n" + //
                     "        PARTITION P3 VALUES LESS THAN(2451544),\n" + //
                     "        PARTITION P4 VALUES LESS THAN(2451910),\n" + //
                     "        PARTITION P5 VALUES LESS THAN(2452275),\n" + //
                     "        PARTITION P6 VALUES LESS THAN(2452640),\n" + //
                     "        PARTITION P7 VALUES LESS THAN(2453005),\n" + //
                     "        PARTITION P8 VALUES LESS THAN(MAXVALUE) TABLESPACE example2\n" +//
                     ")";

        List<ResObject> resList = parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_db/test_schema/abc/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_db/test_schema/abc/id/");
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
            PgTableDomain domain1 = (PgTableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.CREATE_TABLE && domain1.getAuditKind() == SecQueryKind.CREATE;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("abc") &&//
                   !domain1.isIfNotExists() && domain1.getOptions().isEmpty();
        }
        {
            PgColumnDomain domain = (PgColumnDomain) domainList.get(1);
            assert domain.getSqlType() == SecQueryType.CREATE_TABLE_ADD_COLUMN && domain.getAuditKind() == SecQueryKind.CREATE;
            assert domain.getCatalog() == null && domain.getSchema() == null && domain.getTable().equals("abc") &&//
                   domain.getColumn().equals("id") && domain.getTypeDesc().equals("int") && domain.getTypeName().equals("int");
            assert domain.getComment() == null;
        }
    }
}
