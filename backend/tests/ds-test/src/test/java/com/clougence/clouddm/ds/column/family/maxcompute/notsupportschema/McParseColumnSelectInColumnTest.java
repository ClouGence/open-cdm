package com.clougence.clouddm.ds.column.family.maxcompute.notsupportschema;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class McParseColumnSelectInColumnTest extends McSelectColumnTestSupport {

    public McParseColumnSelectInColumnTest(){
        spi = new McSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select (select column1 from table2) as ww from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
    }

}
