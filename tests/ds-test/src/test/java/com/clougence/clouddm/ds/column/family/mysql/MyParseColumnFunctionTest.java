package com.clougence.clouddm.ds.column.family.mysql;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class MyParseColumnFunctionTest extends MySelectColumnTestSupport {

    public MyParseColumnFunctionTest(){
        spi = new MySelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(1) from table1", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("test_function(1)");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(column1) from table1", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("test_function(column1)");
        assert selectItem.getColumns().size() == 1;
        List<RealColumn> columns = selectItem.getColumns();
        RealColumn realColumn = columns.get(0);
        assert realColumn.getColumn().equals("column1");
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(32)", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("test_function(32)");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select cast(column1 as char) as f from table1", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("f");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select f from (select cast(column1 as char) as f from table1)c", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        //        assert selectColumn.getTableAlias() == null && selectColumn.getColumnAlias() == null;
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_func((select column1 from table2)) from table1", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        //        assert selectColumn.getTableAlias() == null && selectColumn.getColumnAlias() == null;
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2");
    }

    @Test
    public void test7() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn("select test_func((select test_func1((select column1 from schema2.table1),column1) from table2),column1) from table1", createContextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        //        assert selectColumn.getTableAlias() == null && selectColumn.getColumnAlias() == null;
        assert selectItem.getColumns().size() == 3;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2");
        realColumn = selectItem.getColumns().get(2);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
    }

}
