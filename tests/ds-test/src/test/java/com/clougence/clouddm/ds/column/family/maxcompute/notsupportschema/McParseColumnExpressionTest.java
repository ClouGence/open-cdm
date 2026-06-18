package com.clougence.clouddm.ds.column.family.maxcompute.notsupportschema;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class McParseColumnExpressionTest extends McSelectColumnTestSupport {

    public McParseColumnExpressionTest(){
        spi = new McSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + 1 as w", contextInfo());

        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("w");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + column1 as qq from table2", contextInfo());

        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("qq");

        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + column1 + (select column1 from table2 limit 1) as `xx` from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("xx");
        assert selectItem.getColumns().size() == 2;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + column1 + (select column1 from table2 limit 1) + test(column2) as mbdv from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("mbdv");
        assert selectItem.getColumns().size() == 3;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
        realColumn = selectItem.getColumns().get(2);
        assert realColumn.getColumn().equals("column2") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 32 + column1 + test(2) + (select column1 from table2) as test from catalog2.table2 a", contextInfo());
        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getItemAlias().equals("test");

        List<RealColumn> columns = selectItem.getColumns();
        {
            assert columns.size() == 2;

            RealColumn column = columns.get(0);
            assert column.getColumn().equals("column1") && column.getSchema().equals("default") && column.getTable().equals("table2");
            assert column.getCatalog().equals("catalog2");

            RealColumn column2 = columns.get(1);
            assert column2.getColumn().equals("column1") && column2.getSchema().equals("default") && column2.getTable().equals("table2");
            assert column2.getCatalog().equals("catalog1");
        }
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 as f", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("f");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test7() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 as a", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test8() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 as a from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test9() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select column1 = 1 as a from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default")
               && realColumn.getCatalog().equals("catalog1");
    }

    @Test
    public void test10() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select column1 + column2 = 1 as a from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().size() == 2;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column2") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("default");
    }
}
