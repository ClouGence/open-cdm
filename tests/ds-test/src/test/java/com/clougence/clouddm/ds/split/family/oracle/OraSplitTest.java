package com.clougence.clouddm.ds.split.family.oracle;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.oracle.analysis.OraSplitAnalysisSpi;
import com.clougence.clouddm.ds.split.SplitTestSupport;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class OraSplitTest extends SplitTestSupport {

    public OraSplitTest(){
        this.splitAnalysisSpi = new OraSplitAnalysisSpi();
    }

    @Test
    public void test1() {
        String sql = "select * from table1;select * from table2;";
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        SplitScript splitScript = splitScripts.get(0);
        assert splitScript.getScript().equals("select * from table1;");
        assert splitScript.getType() == SecQueryType.SELECT;

        splitScript = splitScripts.get(1);
        assert splitScript.getScript().equals("select * from table2;");
        assert splitScript.getType() == SecQueryType.SELECT;
    }

    @Test
    public void test2() {
        String sql = "    select * from table1     ;     select * from table2     ;     ";
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        SplitScript splitScript = splitScripts.get(0);
        assert splitScript.getScript().equals("select * from table1     ;");
        assert splitScript.getType() == SecQueryType.SELECT;

        splitScript = splitScripts.get(1);
        assert splitScript.getScript().equals("select * from table2     ;");
        assert splitScript.getType() == SecQueryType.SELECT;
    }

    @Test
    public void test3() {
        String sql = " -- test \n   select * from table1  -- test comment  \n ;  /*comment\ngweg*/   select * from table2   -- ffff  ;  --aaaa   ";
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        SplitScript splitScript = splitScripts.get(0);
        assert splitScript.getScript().equals("-- test \n   select * from table1  -- test comment  \n ;");
        assert splitScript.getType() == SecQueryType.SELECT;

        splitScript = splitScripts.get(1);
        assert splitScript.getScript().equals("/*comment\ngweg*/   select * from table2   -- ffff  ;  --aaaa   ");
        assert splitScript.getType() == SecQueryType.SELECT;
    }

    @Test
    public void test4() {
        String sql = "comment on column abc.column1 is 'ccc';create global temporary table test.abc(id int) ";
        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
        assert splitScripts.size() == 2;
        SplitScript splitScript = splitScripts.get(0);
        assert splitScript.getScript().equals("comment on column abc.column1 is 'ccc';");
        assert splitScript.getType() == SecQueryType.COMMENT_COLUMN;

        splitScript = splitScripts.get(1);
        assert splitScript.getScript().equals("create global temporary table test.abc(id int)");
        assert splitScript.getType() == SecQueryType.CREATE_TABLE;
    }
}
