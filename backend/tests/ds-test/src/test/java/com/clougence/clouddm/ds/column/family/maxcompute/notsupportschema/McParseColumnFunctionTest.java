package com.clougence.clouddm.ds.column.family.maxcompute.notsupportschema;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class McParseColumnFunctionTest extends McSelectColumnTestSupport {

    public McParseColumnFunctionTest(){
        spi = new McSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(1) as e from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("e");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(column1) as g from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("g");
        assert selectItem.getColumns().size() == 1;
        List<RealColumn> columns = selectItem.getColumns();
        RealColumn realColumn = columns.get(0);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(32) as ge", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("ge");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select cast(column1 as char) as f from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("f");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("default") && realColumn.getTable().equals("table2");
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select f from (select cast(column1 as char) as f from table2)c", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("f");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("default") && realColumn.getTable().equals("table2");
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_func((select column1 from table2)) as vv from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("vv");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("default") && realColumn.getTable().equals("table2");
    }

    @Test
    public void test7() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn("select test_func((select test_func1((select column1 from catalog2.table2),column1) as q from table2),column1) as `xx` from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("xx");
        assert selectItem.getColumns().size() == 3;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getCatalog().equals("catalog2") &&  realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("default") && realColumn.getTable().equals("table2");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("default") && realColumn.getTable().equals("table2");
        realColumn = selectItem.getColumns().get(2);
        assert realColumn.getCatalog().equals("catalog1") &&  realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("default") && realColumn.getTable().equals("table2");
    }

}
