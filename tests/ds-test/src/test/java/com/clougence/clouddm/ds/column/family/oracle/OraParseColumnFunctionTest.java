package com.clougence.clouddm.ds.column.family.oracle;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.oracle.analysis.OraSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class OraParseColumnFunctionTest extends OraSelectColumnTestSupport {

    public OraParseColumnFunctionTest(){
        spi = new OraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(1) as q from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("q");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(column1) as ww from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("ww");
        assert selectItem.getColumns().size() == 1;
        List<RealColumn> columns = selectItem.getColumns();
        RealColumn realColumn = columns.get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_function(32) as a", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        //        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("test_function(32)");
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select cast(column1 as char) as f from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("f");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select f from (select cast(column1 as char) as f from table1)c", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("f");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select test_func((select column1 from table2)) as ff from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("ff");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2");
    }

    @Test
    public void test7() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn("select test_func((select test_func1((select column1 from schema2.table1),column1) as ww from table2),column1) as gg from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("gg");
        assert selectItem.getColumns().size() == 3;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2");
        realColumn = selectItem.getColumns().get(2);
        assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
    }

}
