package com.clougence.clouddm.ds.column.family.postgres;

import java.util.List;

import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.sql.postgres.column.PgSelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.column.RealColumn;
import com.clougence.clouddm.sdk.sql.column.SelectItem;

public class PgParseColumnSelectInColumnTest extends PgSelectColumnTestSupport {

    public PgParseColumnSelectInColumnTest(){
        spi = new PgSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn( "select (select column1 from table1) as ww from table2", contextInfo());
        assert selectItems.size() == 1;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getColumn().equals("column1") && realColumn.getTable().equals("table1") && realColumn.getSchema().equals("schema1");
    }

}
