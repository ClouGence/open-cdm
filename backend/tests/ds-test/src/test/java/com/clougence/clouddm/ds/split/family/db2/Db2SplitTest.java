//package com.clougence.clouddm.ds.split.family.db2;
//
//import java.util.List;
//
//import org.junit.Test;
//
//import com.clougence.clouddm.ds.split.SplitTestSupport;
//import com.clougence.clouddm.dsfamily.db2.analysis.Db2SplitAnalysisSpi;
//import com.clougence.clouddm.sdk.analysis.split.SplitScript;
//import com.clougence.clouddm.sdk.security.auth.SecQueryType;
//
//public class Db2SplitTest extends SplitTestSupport {
//
//    public Db2SplitTest(){
//        this.splitAnalysisSpi = new Db2SplitAnalysisSpi();
//    }
//
//    @Test
//    public void test1() {
//        String sql = "select * from table1;select * from table2;";
//        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
//        assert splitScripts.size() == 2;
//        SplitScript splitScript = splitScripts.get(0);
//        assert splitScript.getScript().equals("select * from table1");
//        assert splitScript.getType() == SecQueryType.SELECT;
//
//        splitScript = splitScripts.get(1);
//        assert splitScript.getScript().equals("select * from table2");
//        assert splitScript.getType() == SecQueryType.SELECT;
//    }
//
//    @Test
//    public void test2() {
//        String sql = "    select * from table1     ;     select * from table2     ;     ";
//        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
//        assert splitScripts.size() == 2;
//        SplitScript splitScript = splitScripts.get(0);
//        assert splitScript.getScript().equals("select * from table1");
//        assert splitScript.getType() == SecQueryType.SELECT;
//
//        splitScript = splitScripts.get(1);
//        assert splitScript.getScript().equals("select * from table2");
//        assert splitScript.getType() == SecQueryType.SELECT;
//    }
//
//    @Test
//    public void test3() {
//        String sql = " -- test \n   select * from table1  -- test comment  \n ;  /*comment\ngweg*/   select * from table2   -- ffff  ;  --aaaa   ";
//        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
//        assert splitScripts.size() == 2;
//        SplitScript splitScript = splitScripts.get(0);
//        assert splitScript.getScript().equals("-- test \n   select * from table1  -- test comment  \n");
//        assert splitScript.getType() == SecQueryType.SELECT;
//
//        splitScript = splitScripts.get(1);
//        assert splitScript.getScript().equals("/*comment\ngweg*/   select * from table2   -- ffff  ;  --aaaa   ");
//        assert splitScript.getType() == SecQueryType.SELECT;
//    }
//
//    @Test
//    public void test4() {
//        String sql = "DROP schema sampledb;" +//
//                "CREATE schema sampledb;" +//
//                "CREATE TABLE employees (\n    id INT NOT NULL,\n    name VARCHAR(100),\n    position VARCHAR(100),\n    salary DECIMAL(10,2),\n    PRIMARY KEY (id)\n);" +//
//                "INSERT INTO employees (id, name, position, salary) VALUES (1, '张三', '软件工程师', 75000);" +//
//                "UPDATE employees SET salary = 80000 WHERE id = 1;" +//
//                "DELETE FROM employees WHERE id = 1;" +//
//                "SELECT * FROM employees;" +//
//                "CREATE INDEX idx_name ON employees(name);" +//
//                "DROP TABLE employees;";
//        List<SplitScript> splitScripts = this.splitAnalysisSpi.splitScript(sql, null, 0, 0);
//        assert splitScripts.size() == 9;
//        SplitScript splitScript = splitScripts.get(0);
//        assert splitScript.getScript().equals("DROP schema sampledb");
//        assert splitScript.getType() == SecQueryType.DROP_SCHEMA;
//
//        splitScript = splitScripts.get(1);
//        assert splitScript.getScript().equals("CREATE schema sampledb");
//        assert splitScript.getType() == SecQueryType.CREATE_SCHEMA;
//
//        splitScript = splitScripts.get(2);
//        assert splitScript.getScript().equals("CREATE TABLE employees (\n    id INT NOT NULL,\n    name VARCHAR(100),\n    position VARCHAR(100),\n    salary DECIMAL(10,2),\n    PRIMARY KEY (id)\n)");
//        assert splitScript.getType() == SecQueryType.CREATE_TABLE;
//
//        splitScript = splitScripts.get(3);
//        assert splitScript.getScript().equals("INSERT INTO employees (id, name, position, salary) VALUES (1, '张三', '软件工程师', 75000)");
//        assert splitScript.getType() == SecQueryType.INSERT;
//
//        splitScript = splitScripts.get(4);
//        assert splitScript.getScript().equals("UPDATE employees SET salary = 80000 WHERE id = 1");
//        assert splitScript.getType() == SecQueryType.UPDATE;
//
//        splitScript = splitScripts.get(5);
//        assert splitScript.getScript().equals("DELETE FROM employees WHERE id = 1");
//        assert splitScript.getType() == SecQueryType.DELETE;
//
//        splitScript = splitScripts.get(6);
//        assert splitScript.getScript().equals("SELECT * FROM employees");
//        assert splitScript.getType() == SecQueryType.SELECT;
//
//        splitScript = splitScripts.get(7);
//        assert splitScript.getScript().equals("CREATE INDEX idx_name ON employees(name)");
//        assert splitScript.getType() == SecQueryType.CREATE_INDEX;
//
//        splitScript = splitScripts.get(8);
//        assert splitScript.getScript().equals("DROP TABLE employees");
//        assert splitScript.getType() == SecQueryType.DROP_TABLE;
//    }
//}
