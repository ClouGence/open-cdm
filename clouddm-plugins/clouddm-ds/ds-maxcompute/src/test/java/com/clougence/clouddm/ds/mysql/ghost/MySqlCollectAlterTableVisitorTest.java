//package com.clougence.clouddm.ds.mysql.ghost;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.alibaba.druid.sql.ast.SQLStatement;
//import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
//import com.alibaba.druid.sql.parser.SQLParserFeature;
//import com.alibaba.druid.sql.parser.SQLStatementParser;
//import com.alibaba.druid.sql.visitor.VisitorFeature;
//
///**
// * @author bucketli 2022/6/14 10:08:25
// */
//public class MySqlCollectAlterTableVisitorTest {
//
//    @Test
//    public void testQuote() {
//        String ddlSql = "alter table `abc` add column `zzz` varchar(255) not null default '1111'";
//        SQLStatementParser parser = new MySqlStatementParser(ddlSql, SQLParserFeature.IgnoreNameQuotes);
//        SQLStatement statement = parser.parseAlter();
//
//        if (statement == null) {
//            throw new IllegalArgumentException("illegal alter sql:" + ddlSql);
//        }
//
//        StringBuilder tabBuilder = new StringBuilder();
//        StringBuilder dbBuilder = new StringBuilder();
//        StringBuilder sqlBuilder = new StringBuilder();
//
//        MySqlCollectAlterTableVisitor v = new MySqlCollectAlterTableVisitor(sqlBuilder, tabBuilder, dbBuilder);
//        v.config(VisitorFeature.OutputNameQuote, true);
//        v.config(VisitorFeature.OutputPrettyFormat, false);
//        statement.accept(v);
//
//        Assert.assertEquals("ALTER TABLE \\`abc\\` ADD COLUMN \\`zzz\\` varchar(255) NOT NULL DEFAULT '1111'", sqlBuilder.toString());
//        Assert.assertEquals("abc", tabBuilder.toString());
//        Assert.assertEquals("", dbBuilder.toString());
//    }
//
//    @Test
//    public void testWithoutQuote() {
//        String ddlSql = "alter table abc add column `zzz` varchar(255) not null default '1111'";
//        SQLStatementParser parser = new MySqlStatementParser(ddlSql, SQLParserFeature.IgnoreNameQuotes);
//        SQLStatement statement = parser.parseAlter();
//
//        if (statement == null) {
//            throw new IllegalArgumentException("illegal alter sql:" + ddlSql);
//        }
//
//        StringBuilder tabBuilder = new StringBuilder();
//        StringBuilder dbBuilder = new StringBuilder();
//        StringBuilder sqlBuilder = new StringBuilder();
//
//        MySqlCollectAlterTableVisitor v = new MySqlCollectAlterTableVisitor(sqlBuilder, tabBuilder, dbBuilder);
//        v.config(VisitorFeature.OutputNameQuote, true);
//        v.config(VisitorFeature.OutputPrettyFormat, false);
//        statement.accept(v);
//
//        Assert.assertEquals("ALTER TABLE \\`abc\\` ADD COLUMN \\`zzz\\` varchar(255) NOT NULL DEFAULT '1111'", sqlBuilder.toString());
//        Assert.assertEquals("abc", tabBuilder.toString());
//        Assert.assertEquals("", dbBuilder.toString());
//    }
//}
