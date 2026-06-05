package com.clougence.clouddm.ds.column.family.maxcompute.supportschema;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.ds.maxcompute.analysis.McSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class McParseColumnJoinTest extends McSelectColumnTestSupport {

    public McParseColumnJoinTest(){
        spi = new McSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select * from table1 left join catalog2.schema2.table2 on table1.column1 = table2.column1", contextInfo());
        assert selectItems.size() == 12;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1");
            assert selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
        for (int i = 7; i <= 12; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table2") && selectItem.getItemAlias().equals("column" + (i - 6));
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + (i - 6)) && column.getSchema().equals("schema2") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog2");
            }
        }
    }

    @Test
    public void test1_1() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn( "select table1.column1,table2.column2 from table1 left join table2 on table1.column1 = table2.column1", contextInfo());
        assert selectItems.size() == 2;

        SelectItem selectItem = selectItems.get(0);
        //        assert selectColumn.getTableAlias().equals("table1");
        assert selectItem.getItemAlias().equals("column1");
        RealColumn column = selectItem.getColumns().get(0);
        assert column.getColumn().equals("column1") && column.getSchema().equals("schema1") && column.getTable().equals("table1");
        assert column.getCatalog().equals("catalog1");

        selectItem = selectItems.get(1);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column2");
        column = selectItem.getColumns().get(0);

        assert column.getColumn().equals("column2") && column.getSchema().equals("schema1") && column.getTable().equals("table2");
        assert column.getCatalog().equals("catalog1");
    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select * from table1 a left join table2 b on a.column1 = b.column1", contextInfo());
        assert selectItems.size() == 12;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
        for (int i = 7; i <= 12; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("b") && selectItem.getItemAlias().equals("column" + (i - 6));
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + (i - 6)) && column.getSchema().equals("schema1") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn(  "select * from table1 left join schema2.table2 on table1.column1 = table2.column1", contextInfo());
        assert selectItems.size() == 12;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table1") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
        for (int i = 7; i <= 12; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("table2") && selectItem.getItemAlias().equals("column" + (i - 6));
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + (i - 6)) && column.getSchema().equals("schema2") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select * from table1 a left join schema2.table2 b on a.column1 = b.column1", contextInfo());
        assert selectItems.size() == 12;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
        for (int i = 7; i <= 12; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("b") && selectItem.getItemAlias().equals("column" + (i - 6));
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + (i - 6)) && column.getSchema().equals("schema2") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test5() {
        List<SelectItem> selectItems = spi.parseSelectColumn(  "select a.*,b.* from table1 a left join schema2.table2 b on a.column1 = b.column1", contextInfo());
        assert selectItems.size() == 12;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }
        for (int i = 7; i <= 12; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("b") && selectItem.getItemAlias().equals("column" + (i - 6));
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + (i - 6)) && column.getSchema().equals("schema2") && column.getTable().equals("table2");
                assert column.getCatalog().equals("catalog1");
            }
        }
    }

    @Test
    public void test6() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select a.*,b.column1 from table1 a left join schema2.table2 b on a.column1 = b.column1", contextInfo());
        assert selectItems.size() == 7;
        for (int i = 1; i <= 6; i++) {
            SelectItem selectItem = selectItems.get(i - 1);
            assert selectItem.getTableAlias().equals("a") && selectItem.getItemAlias().equals("column" + i);
            for (RealColumn column : selectItem.getColumns()) {
                assert column.getColumn().equals("column" + i) && column.getSchema().equals("schema1") && column.getTable().equals("table1");
                assert column.getCatalog().equals("catalog1");
            }
        }

        SelectItem selectItem = selectItems.get(6);
        assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column1");
        for (RealColumn column : selectItem.getColumns()) {
            assert column.getColumn().equals("column1") && column.getSchema().equals("schema2") && column.getTable().equals("table2");
            assert column.getCatalog().equals("catalog1");
        }
    }

    @Test
    public void test7() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn( "select column1,d from (select a.*,b.column1 as d from table1 a left join schema2.table2 b on a.column1 = b.column1)c", contextInfo());
        assert selectItems.size() == 2;

        {
            SelectItem selectItem = selectItems.get(0);
            assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column1");
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
            assert realColumn.getCatalog().equals("catalog1");
        }

        {
            SelectItem selectItem = selectItems.get(1);
            assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("d");
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table2");
            assert realColumn.getCatalog().equals("catalog1");
        }
    }

    @Test
    public void test8() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn(  "select a.column1,b.column2,c.column3 from table1 a left join table2 b on a.column1 = b.column1 left join schema2.table1 c on c.column1 = a.column1", contextInfo());
        assert selectItems.size() == 3;

        {
            SelectItem selectItem = selectItems.get(0);
            assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column1");
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getColumn().equals("column1") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1");
            assert realColumn.getCatalog().equals("catalog1");
        }

        {
            SelectItem selectItem = selectItems.get(1);
            assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column2");
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getColumn().equals("column2") && realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2");
            assert realColumn.getCatalog().equals("catalog1");
        }
        {
            SelectItem selectItem = selectItems.get(2);
            assert selectItem.getTableAlias() == null && selectItem.getItemAlias().equals("column3");
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getColumn().equals("column3") && realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1");
            assert realColumn.getCatalog().equals("catalog1");
        }
    }
}
