package com.clougence.clouddm.ds.column.family.starrocks;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.starrocks.analysis.SrSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class SrParseColumnSimpleTest extends SrSelectColumnTestSupport {

    public SrParseColumnSimpleTest(){
        spi = new SrSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn(  "select * from table1", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test1_1() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select * from (select * from table1) a", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test1_2() {
        List<SelectItem> selectItems = spi.parseSelectColumn(  "select * from (select * from table1) c", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("c") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select * from table1 a", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select column1 from table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column1");
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select column1 as test from table1 a", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getItemAlias().equals("test");
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn(  "select * from schema2.table1", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema2") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select * from schema2.table1 a", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema2") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test7() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select column1 from schema2.table1", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column1");
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema2") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test8() {
        List<SelectItem> selectItems = spi.parseSelectColumn(  "select column1 as test from schema2.table1 a", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getItemAlias().equals("test");
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema2") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test9() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select *,test(column1) as s from schema2.table1", contextInfo());
        assert selectItems.size() == 7;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema2") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }

        SelectItem selectItem = selectItems.get(6);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("s");
        assert selectItem.getColumns().size() == 1;
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema2") && column.getTable().equals("table1");
    }

}
