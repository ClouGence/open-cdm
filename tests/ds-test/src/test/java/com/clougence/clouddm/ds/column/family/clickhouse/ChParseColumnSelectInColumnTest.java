package com.clougence.clouddm.ds.column.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.ds.clickhouse.analysis.ChSelectColumnAnalysisSpi;
import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class ChParseColumnSelectInColumnTest extends ChSelectColumnTestSupport {

    public ChParseColumnSelectInColumnTest(){
        spi = new ChSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select (select column1 from table1) as x from table2", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }

}
