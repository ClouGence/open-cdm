package com.clougence.clouddm.ds.polardb.analysis.porx;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.ds.polardb.parser.porx.antlr.PolardbXParser;

public class PorXSqlParserVisitor extends AbstractPorXSQLParserVisitor {

    public PorXSqlParserVisitor(MyBuilderFactory builder, Parser parser){
        super(builder, parser);
    }

    @Override
    public Void visitPolardbXPartitionGlobalIndex(PolardbXParser.PolardbXPartitionGlobalIndexContext ctx) {
        builder.enterCreateIndex();
        if (ctx.indexColumnNames().size() == 2) {
            for (ParseTree child : ctx.children) {
                if (child == ctx.indexColumnNames(1)) {
                    continue;
                } else {
                    child.accept(this);
                }
            }
        } else {
            dmVisitChildren(ctx);
        }

        builder.exitCreateIndex();
        return null;
    }

    @Override
    public Void visitPolardbxPartitionOption(PolardbXParser.PolardbxPartitionOptionContext ctx) {
        return null;
    }

    @Override
    public Void visitPartitionDefinitions(PolardbXParser.PartitionDefinitionsContext ctx) {
        return null;
    }

    @Override
    public Void visitPolardbXAlterSpecification(PolardbXParser.PolardbXAlterSpecificationContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_INDEX);
        builder.enterCreateIndex();
        ctx.indexName().accept(this);
        ctx.indexColumnNames(0).accept(this);

        if (ctx.UNIQUE() != null) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        }
        builder.exitCreateIndex();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitTableLocalityOption(PolardbXParser.TableLocalityOptionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableBroadcastOption(PolardbXParser.TableBroadcastOptionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDbShardingAlgorithm(PolardbXParser.DbShardingAlgorithmContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

}
