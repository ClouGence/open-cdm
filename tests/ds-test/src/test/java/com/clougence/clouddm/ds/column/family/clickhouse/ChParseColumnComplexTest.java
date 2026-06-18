package com.clougence.clouddm.ds.column.family.clickhouse;

import java.util.List;

import com.clougence.clouddm.ds.clickhouse.analysis.ChSelectColumnAnalysisSpi;
import org.junit.Test;

import com.clougence.clouddm.ds.column.TestMetaServiceImpl;
import com.clougence.clouddm.dsfamily.mysql.analysis.MySelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;

public class ChParseColumnComplexTest extends ChSelectColumnTestSupport {

    public ChParseColumnComplexTest(){
        spi = new ChSelectColumnAnalysisSpi(new TestMetaServiceImpl());
    }

    @Test
    public void test1() {
        List<SelectItem> selectItems = spi.parseSelectColumn("-- 包含子查询的跨Schema三表连接[1,7](@ref)\n" //
                                                             + "SELECT s1t1.column1 AS main_id,\n" + //
                                                             "       (SELECT MAX(s2t2.column3) as x \n"//
                                                             + "        FROM schema2.table2 s2t2 \n" + //
                                                             "        WHERE s2t2.column4 = s1t1.column2) AS max_val,\n"//
                                                             + "       RANK() OVER (ORDER BY s1t2.column5 DESC) AS rank_level\n" //
                                                             + "FROM schema1.table1 s1t1\n" //
                                                             + "JOIN schema1.table2 s1t2 \n"//
                                                             + "  ON s1t1.column3 = s1t2.column1\n" + "LEFT JOIN schema2.table1 s2t1 \n" //
                                                             + "  ON s1t1.column4 = s2t1.column5\n"//
                                                             + "WHERE s2t1.column6 IN (\n" + //
                                                             "    SELECT DISTINCT column2 \n" //
                                                             + "    FROM schema2.table2 \n" //
                                                             + "    WHERE column4 > 100\n"//
                                                             + ");", createContextInfo());
        assert selectItems.size() == 3;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getItemAlias().equals("main_id");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column1");

        selectItem = selectItems.get(1);
        assert selectItem.getItemAlias().equals("max_val");
        assert selectItem.getColumns().size() == 1;
        realColumn = selectItem.getColumns().get(0);
        assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table2") && realColumn.getColumn().equals("column3");

        selectItem = selectItems.get(2);
        assert selectItem.getItemAlias().equals("rank_level");
        assert selectItem.getColumns().isEmpty();

    }

    @Test
    public void test2() {
        List<SelectItem> selectItems = spi.parseSelectColumn("SELECT s1t1.column2 AS category,\n" +//
                                                             "       SUM(s2t1.column4) AS total_sum,\n" +//
                                                             "       AVG(s1t2.column5) OVER (PARTITION BY s1t1.column2) AS avg_value,\n" +//
                                                             "       (SELECT COUNT(*) as x \n" +//
                                                             "        FROM schema2.table2 \n" +//
                                                             "        WHERE column3 = s1t1.column3) AS related_count\n" +//
                                                             "FROM schema1.table1 s1t1\n" +//
                                                             "LEFT JOIN schema1.table2 s1t2 \n" +//
                                                             "  ON s1t1.column1 = s1t2.column6\n" +//
                                                             "RIGHT JOIN schema2.table1 s2t1 \n" +//
                                                             "  ON s1t1.column4 = s2t1.column3\n" +//
                                                             "GROUP BY s1t1.column2\n" +//
                                                             "HAVING total_sum > (\n" +//
                                                             "    SELECT AVG(column4)*1.5 \n" +//
                                                             "    FROM schema2.table1\n" +//
                                                             ");", createContextInfo());
        assert selectItems.size() == 4;
        SelectItem selectItem = selectItems.get(0);
        assert selectItem.getItemAlias().equals("category");
        assert selectItem.getColumns().size() == 1;
        RealColumn realColumn = selectItem.getColumns().get(0);
        assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column2");

        selectItem = selectItems.get(1);
        assert selectItem.getItemAlias().equals("total_sum");
        assert selectItem.getColumns().size() == 1;
        realColumn = selectItem.getColumns().get(0);
        assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column4");

        selectItem = selectItems.get(2);
        assert selectItem.getItemAlias().equals("avg_value");
        assert selectItem.getColumns().size() == 1;
        realColumn = selectItem.getColumns().get(0);
        assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2") && realColumn.getColumn().equals("column5");

        selectItem = selectItems.get(3);
        assert selectItem.getItemAlias().equals("related_count");
        assert selectItem.getColumns().isEmpty();

    }

    @Test
    public void test3() {
        List<SelectItem> selectItems = spi.parseSelectColumn("(\n" +//
                                                             "    SELECT \n" +//
                                                             "        s1t1.*,\n" +//
                                                             "        s2t2.column1 AS s2_col1,\n" +//
                                                             "        ROW_NUMBER() OVER (ORDER BY s1t2.column5) AS conflict_alias\n" +//
                                                             "    FROM schema1.table1 s1t1\n" +//
                                                             "    LEFT JOIN schema2.table2 s2t2 \n" +//
                                                             "      ON s1t1.column6 = s2t2.column3\n" +//
                                                             "    LEFT JOIN schema1.table2 s1t2 \n" +//
                                                             "      ON s1t1.column2 = s1t2.column4\n" +//
                                                             ") \n" +//
                                                             "UNION all\n" +//
                                                             "(\n" +//
                                                             "    SELECT \n" +//
                                                             "        s2t1.*,\n" +//
                                                             "        s1t2.column2 AS s1_col2,\n" +//
                                                             "        RANK() OVER (PARTITION BY s2t1.column3 ORDER BY s2t1.column4) AS conflict_alias \n" +//
                                                             "    FROM schema2.table1 s2t1\n" +//
                                                             "    RIGHT JOIN schema1.table2 s1t2 \n" +//
                                                             "      ON s2t1.column5 = s1t2.column1\n" +//
                                                             "    WHERE s2t1.column6 IN (\n" +//
                                                             "        SELECT MAX(column6) \n" +//
                                                             "        FROM schema2.table2 \n" +//
                                                             "        GROUP BY column2\n" +//
                                                             "    )\n" +//
                                                             ");", createContextInfo());

        assert selectItems.size() == 8;
        {
            SelectItem selectItem = selectItems.get(0);
            assert selectItem.getItemAlias().equals("column1");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column1");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column1");
        }
        {

            SelectItem selectItem = selectItems.get(1);
            assert selectItem.getItemAlias().equals("column2");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column2");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column2");
        }
        {

            SelectItem selectItem = selectItems.get(2);
            assert selectItem.getItemAlias().equals("column3");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column3");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column3");
        }
        {

            SelectItem selectItem = selectItems.get(3);
            assert selectItem.getItemAlias().equals("column4");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column4");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column4");
        }
        {

            SelectItem selectItem = selectItems.get(4);
            assert selectItem.getItemAlias().equals("column5");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column5");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column5");
        }
        {

            SelectItem selectItem = selectItems.get(5);
            assert selectItem.getItemAlias().equals("column6");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column6");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column6");
        }
        {

            SelectItem selectItem = selectItems.get(6);
            assert selectItem.getItemAlias().equals("s2_col1");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table2") && realColumn.getColumn().equals("column1");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2") && realColumn.getColumn().equals("column2");
        }
        {

            SelectItem selectItem = selectItems.get(7);
            assert selectItem.getItemAlias().equals("conflict_alias");
            assert selectItem.getColumns().isEmpty();
        }
    }

    @Test
    public void test4() {
        List<SelectItem> selectItems = spi
            .parseSelectColumn("select a.column1,b.column2 from (select * from table1 union all select * from table2) a "
                               + "left join (select * from schema2.table1 union all select * from schema2.table2) b on a.column1 = b.column1", createContextInfo());

        assert selectItems.size() == 2;
        {
            SelectItem selectItem = selectItems.get(0);
            assert selectItem.getItemAlias().equals("column1");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column1");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema1") && realColumn.getTable().equals("table2") && realColumn.getColumn().equals("column1");
        }
        {

            SelectItem selectItem = selectItems.get(1);
            assert selectItem.getItemAlias().equals("column2");
            assert selectItem.getColumns().size() == 2;
            RealColumn realColumn = selectItem.getColumns().get(0);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table1") && realColumn.getColumn().equals("column2");

            realColumn = selectItem.getColumns().get(1);
            assert realColumn.getSchema().equals("schema2") && realColumn.getTable().equals("table2") && realColumn.getColumn().equals("column2");
        }

    }
}
