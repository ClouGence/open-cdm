package com.clougence.clouddm.ds.column.family.maxcompute.supportschema;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class McParseColumnUnionTest extends McSelectColumnTestSupport {

    public McParseColumnUnionTest(){
        spi = new McSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select * from table1 union select * from table2", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1") && selectItem.getItemAlias().equals("column" + i);
            List<RealColumn> columns = selectItem.getColumns();
            RealColumn column = columns.get(0);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
            assert column.getCatalog().equals("catalog1");

            column = columns.get(1);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table2");
            assert column.getCatalog().equals("catalog1");
        }
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select column1 from (select column1  from table1 union select column2 from table2)c", contextInfo());
        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column1");
        List<RealColumn> columns = selectItem.getColumns();
        assert columns.size() == 2;
        RealColumn column = columns.get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");

        column = columns.get(1);
        assert column.getColumn().equals("column2") && column.getSchema().equals("schema1") && column.getTable().equals("table2");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select e from (select column1 as e  from table1 union select column2 as x from table2)c", contextInfo());
        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("e");
        List<RealColumn> columns = selectItem.getColumns();
        assert columns.size() == 2;
        RealColumn column = columns.get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");

        column = columns.get(1);
        assert column.getColumn().equals("column2") && column.getSchema().equals("schema1") && column.getTable().equals("table2");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select * from table1 union select * from table2 union select * from schema2.table1", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1") && selectItem.getItemAlias().equals("column" + i);
            List<RealColumn> columns = selectItem.getColumns();
            RealColumn column = columns.get(0);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
            assert column.getCatalog().equals("catalog1");

            column = columns.get(1);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table2");
            assert column.getCatalog().equals("catalog1");

            column = columns.get(2);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema2") && column.getTable().equals("table1");
            assert column.getCatalog().equals("catalog1");

        }
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select * from (select * from table1 union select * from table2) c", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("c") && selectItem.getItemAlias().equals("column" + i);
            List<RealColumn> columns = selectItem.getColumns();
            RealColumn column = columns.get(0);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
            assert column.getCatalog().equals("catalog1");

            column = columns.get(1);
            assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table2");
            assert column.getCatalog().equals("catalog1");
        }
    }
}
