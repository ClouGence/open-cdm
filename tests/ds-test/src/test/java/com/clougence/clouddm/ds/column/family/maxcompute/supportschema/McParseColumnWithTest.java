package com.clougence.clouddm.ds.column.family.maxcompute.supportschema;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class McParseColumnWithTest extends McSelectColumnTestSupport {

    public McParseColumnWithTest(){
        spi = new McSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("with test1 as (select * from table1) select * from test1", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("test1") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn("with test1 as (select * from table1) select * from (with test2 as (select * from table2) select * from test2)", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn("select * from (with test1 as (select * from table1) select * from (with test1 as (select * from table2) select * from test1)) t", contextInfo());
        assert selectItems.size() == 6;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("t") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn("with test1 (a,b,c,d,e,f) as (select * from table1) select a from test1", contextInfo());
        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias().equals("test1") && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().size() == 1;
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog() == null;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn("select * from (with test1 (a,b,c,d,e,f) as (select * from table1) select a from test1) c", contextInfo());
        assert selectItems.size() == 1;

        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getTableAlias().equals("c") && selectItem.getItemAlias().equals("a");
        assert selectItem.getColumns().size() == 1;
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog() == null;
    }
}
