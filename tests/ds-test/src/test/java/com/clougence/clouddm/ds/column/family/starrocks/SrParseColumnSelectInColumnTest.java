package com.clougence.clouddm.ds.column.family.starrocks;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.starrocks.analysis.SrSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class SrParseColumnSelectInColumnTest extends SrSelectColumnTestSupport {

    public SrParseColumnSelectInColumnTest(){
        spi = new SrSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select (select column1 from table1) as x from catalog2.schema1.table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1")
               && realColumn.getCatalog().equals("catalog1");
    }

}
