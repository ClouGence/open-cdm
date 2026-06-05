package com.clougence.clouddm.ds.column.family.oracle;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.oracle.analysis.OraSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class OraParseColumnExpressionTest extends OraSelectColumnTestSupport {

    public OraParseColumnExpressionTest(){
        spi = new OraSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + 1 a", contextInfo());

        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        //        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("1 + 1");
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + column1 as xx from table1", contextInfo());

        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        //        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("1 + column1");
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("xx");

        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + column1 + (select column1 from table2 ) as qq from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("qq");
        //        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("1 + column1 + (select column1 from table2 )");
        assert selectItem.getColumns().size() == 2;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("schema1");
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 + column1 + (select column1 from table2 ) + test(column2) as \"ffw\" from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("ffw");
        //        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("1 + column1 + (select column1 from table2 ) + test(column2)");
        assert selectItem.getColumns().size() == 3;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table2") && realColumn.getSchema().equals("schema1");
        realColumn = selectItem.getColumns().get(2);
        assert realColumn.getColumn().equals("column2") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 32 + column1 + test(2) + (select column1 from table2) as test from schema2.table1 a", contextInfo());
        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getItemAlias().equals("test");

        List<RealColumn> columns = selectItem.getColumns();
        {
            assert columns.size() == 2;

            RealColumn column = columns.get(0);
            assert column.getColumn().equals("column1") && column.getSchema().equals("schema2") && column.getTable().equals("table1");
            assert column.getCatalog() == null;

            RealColumn column2 = columns.get(1);
            assert column2.getColumn().equals("column1") && column2.getSchema().equals("schema1") && column2.getTable().equals("table2");
        }
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 as \" fw \"", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals(" fw ");
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
        List<SelectItem> selectItems = spi.parseSelectColumn("select 1 as a from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().isEmpty();
    }

    @Test
    public void test9() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select column1 = 1 as a from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }

    @Test
    public void test10() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select column1 + column2 = 1 as a from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().size() == 2;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
        realColumn = selectItem.getColumns().get(1);
        assert realColumn.getColumn().equals("column2") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }
}
