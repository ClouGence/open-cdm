package com.clougence.clouddm.ds.secdomain.family.db2;

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.junit.Test;

import com.clougence.clouddm.dsfamily.db2.analysis.Db2ResAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SecDomainResolveSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2ColumnDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2TableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.model.analysis.resource.ResObject;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolve4CommonTest extends Db2SecDomainTestSupport {

    public Db2SecDomainResolve4CommonTest(){
        this.analysisSpi = new Db2ResAnalysisSpi();
        this.resolveSpi = new Db2SecDomainResolveSpi();
        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
        this.dataSourceType = DataSourceType.Db2;
    }

    @Test
    public void commentTable_1() {
        String sql = "comment on table table_name is 'ss';";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 1;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_TABLE;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2TableDomain domain1 = (Db2TableDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_TABLE && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }

    @Test
    public void commentColumn_1() {
        String sql = "comment on column table_name.column1 is 'ss';";

        List<ResObject> resList =parserRes(sql);
        assert resList.size() == 2;
        {
            assert resList.get(0).getType() == TargetType.Table &&//
                   resList.get(0).toDsResPath().getResPath().equals("/test_schema/table_name/");
            assert resList.get(1).getType() == TargetType.Column &&//
                   resList.get(1).toDsResPath().getResPath().equals("/test_schema/table_name/column1/");
        }

        //        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        //        assert splitScripts.size() == 1;
        //        {
        //            assert splitScripts.get(0).getScript().equals(sql.substring(0, sql.length() - 1));
        //            assert splitScripts.get(0).getType() == SecQueryType.COMMENT_COLUMN;
        //        }

        List<RuleDomain> domainList = resolveSpi.resolveDomain(dataSourceType, codeInfo(sql), contextInfo());
        assert domainList.size() == 1;
        {
            Db2ColumnDomain domain1 = (Db2ColumnDomain) domainList.get(0);
            assert domain1.getSqlType() == SecQueryType.COMMENT_COLUMN && domain1.getAuditKind() == SecQueryKind.ALTER;
            assert domain1.getCatalog() == null && domain1.getSchema() == null && domain1.getTable().equals("table_name") &&//
                   domain1.getComment().equals("ss");
        }
    }
}
