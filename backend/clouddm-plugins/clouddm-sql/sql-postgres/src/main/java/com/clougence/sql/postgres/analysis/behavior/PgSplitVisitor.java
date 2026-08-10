/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.postgres.analysis.behavior;

import static com.clougence.sql.postgres.parser.antlr.PgSqlParser.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.postgres.analysis.sysobj.PgResourceRegistry;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParserBaseVisitor;

public class PgSplitVisitor extends PgSqlParserBaseVisitor<StatementType> {

    private static final PgResourceRegistry RESOURCES             = PgResourceRegistry.instance();
    private static final Set<String> METADATA_FUNCTIONS    = Set
        .of("acldefault", "aclexplode", "col_description", "format_type", "has_any_column_privilege", "has_column_privilege", "has_database_privilege", "has_foreign_data_wrapper_privilege", "has_function_privilege", "has_language_privilege", "has_largeobject_privilege", "has_parameter_privilege", "has_schema_privilege", "has_sequence_privilege", "has_server_privilege", "has_table_privilege", "has_tablespace_privilege", "has_type_privilege", "makeaclitem", "obj_description", "pg_char_to_encoding", "pg_collation_actual_version", "pg_collation_is_visible", "pg_column_compression", "pg_column_size", "pg_conversion_is_visible", "pg_database_size", "pg_describe_object", "pg_encoding_to_char", "pg_filenode_relation", "pg_function_is_visible", "pg_get_catalog_foreign_keys", "pg_get_constraintdef", "pg_get_expr", "pg_get_function_arguments", "pg_get_function_identity_arguments", "pg_get_function_result", "pg_get_functiondef", "pg_get_indexdef", "pg_get_keywords", "pg_get_object_address", "pg_get_partition_constraintdef", "pg_get_partkeydef", "pg_get_ruledef", "pg_get_serial_sequence", "pg_get_statisticsobjdef", "pg_get_triggerdef", "pg_get_userbyid", "pg_get_viewdef", "pg_has_role", "pg_identify_object", "pg_identify_object_as_address", "pg_index_column_has_property", "pg_index_has_property", "pg_indexam_has_property", "pg_indexes_size", "pg_is_in_recovery", "pg_listening_channels", "pg_opclass_is_visible", "pg_operator_is_visible", "pg_opfamily_is_visible", "pg_partition_ancestors", "pg_partition_root", "pg_partition_tree", "pg_relation_filenode", "pg_relation_filepath", "pg_relation_size", "pg_statistics_obj_is_visible", "pg_table_is_visible", "pg_table_size", "pg_tablespace_databases", "pg_tablespace_location", "pg_tablespace_size", "pg_total_relation_size", "pg_ts_config_is_visible", "pg_ts_dict_is_visible", "pg_ts_parser_is_visible", "pg_ts_template_is_visible", "pg_type_is_visible", "pg_typeof", "row_security_active", "shobj_description", "to_regclass", "to_regcollation", "to_regnamespace", "to_regoper", "to_regoperator", "to_regproc", "to_regprocedure", "to_regrole", "to_regtype");

    private static final Set<String> PERFORMANCE_RELATIONS = Set.of("pg_statistic", "pg_statistic_ext", "pg_statistic_ext_data", "pg_stats", "pg_stats_ext", "pg_stats_ext_exprs");

    private final PostgresVersion    version;
    private final Set<StatementType> types                 = new LinkedHashSet<>();
    private boolean                  metadataReference;
    private boolean                  ordinaryRelation;
    private boolean                  requiresSelectCarrier;
    private boolean                  currentNodeOnly;

    public PgSplitVisitor(){
        this(PostgresVersion.LATEST);
    }

    public PgSplitVisitor(PostgresVersion version){
        this.version = version == null ? PostgresVersion.LATEST : version;
    }

    @Override
    public StatementType visit(ParseTree tree) {
        collectTypes(tree);
        return this.types.stream().findFirst().orElse(null);
    }

    public Set<StatementType> collectTypes(ParseTree tree) {
        this.types.clear();
        this.metadataReference = false;
        this.ordinaryRelation = false;
        this.requiresSelectCarrier = false;
        collectNode(tree);
        if (this.metadataReference && !this.ordinaryRelation && !this.requiresSelectCarrier) {
            this.types.remove(StatementType.SELECT);
        }
        return new LinkedHashSet<>(this.types);
    }

    private void collectNode(ParseTree tree) {
        boolean previous = this.currentNodeOnly;
        StatementType type;
        if (isOwnershipTransfer(tree)) {
            type = StatementType.TRANSFER_PRIVILEGE;
        } else {
            try {
                this.currentNodeOnly = true;
                type = tree.accept(this);
            } finally {
                this.currentNodeOnly = previous;
            }
        }
        if (tree instanceof ExecutestmtContext && hasToken(tree, CREATE)) {
            this.types.add(StatementType.CREATE_TABLE);
        }
        if (type != null) {
            this.types.add(type);
        }
        if (tree instanceof DeclarecursorstmtContext && !isExplainAnalyzeWrapper(tree) || tree instanceof FetchstmtContext || tree instanceof CloseportalstmtContext) {
            this.types.add(StatementType.PROGRAM_CONTROL);
        }
        if (tree instanceof VariablesetstmtContext ctx && ctx.set_rest().SESSION() != null) {
            this.types.add(StatementType.SESSION_SETTING_WRITE);
        }
        if (tree instanceof Func_applicationContext function) {
            collectSystemFunctionTypes(function);
        }
        if (tree instanceof Relation_exprContext relation) {
            collectRelationType(relation);
        }
        if (tree instanceof Func_expr_common_subexprContext expression && hasToken(expression, COLLATION) && hasToken(expression, FOR)) {
            this.metadataReference = true;
            this.types.add(StatementType.METADATA);
        }
        if (!shouldDescend(tree, type)) {
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNode(tree.getChild(i));
        }
    }

    private boolean isOwnershipTransfer(ParseTree tree) {
        return tree instanceof AlterownerstmtContext || tree instanceof AltertablestmtContext && hasToken(tree, OWNER) && hasToken(tree, TO)
               || tree instanceof AlterseqstmtContext && hasToken(tree, OWNER) && hasToken(tree, TO);
    }

    private boolean isExplainAnalyzeWrapper(ParseTree tree) {
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (parent instanceof ExplainstmtContext explain) {
                return isExplainAnalyze(explain);
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean shouldDescend(ParseTree tree, StatementType type) {
        if (tree instanceof ExplainstmtContext ctx) {
            return isExplainAnalyze(ctx);
        }
        if (tree instanceof CreateasstmtContext && !isExplainAnalyzeWrapper(tree) || tree instanceof SelectstmtContext || tree instanceof Select_no_parensContext
            || tree instanceof InsertstmtContext || tree instanceof UpdatestmtContext || tree instanceof DeletestmtContext || tree instanceof MergestmtContext
            || tree instanceof CopystmtContext || tree instanceof AltertablestmtContext || tree instanceof DeclarecursorstmtContext) {
            return true;
        }
        return type == null;
    }

    @Override
    public StatementType visitDostmt(DostmtContext ctx) {
        return StatementType.BLOCK;
    }

    @Override
    public StatementType visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitAnalyzestmt(AnalyzestmtContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitCreateseqstmt(CreateseqstmtContext ctx) {
        return StatementType.CREATE_SEQUENCE;
    }

    @Override
    public StatementType visitTruncatestmt(TruncatestmtContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        return StatementType.ALTER_CATALOG;
    }

    @Override
    public StatementType visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        return StatementType.ALTER_CATALOG;
    }

    @Override
    public StatementType visitRename_table_stmt(Rename_table_stmtContext ctx) {
        return StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitRename_database_stmt(Rename_database_stmtContext ctx) {
        return StatementType.RENAME_CATALOG;
    }

    @Override
    public StatementType visitRename_column_stmt(Rename_column_stmtContext ctx) {
        return StatementType.RENAME_COLUMN;
    }

    @Override
    public StatementType visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        return StatementType.RENAME_SCHEMA;
    }

    @Override
    public StatementType visitComment_table_stmt(Comment_table_stmtContext ctx) {
        return StatementType.COMMENT_TABLE;
    }

    @Override
    public StatementType visitComment_column_stmt(Comment_column_stmtContext ctx) {
        return StatementType.COMMENT_COLUMN;
    }

    @Override
    public StatementType visitCommentstmt(CommentstmtContext ctx) {
        if (hasToken(ctx, LARGE_P) && hasToken(ctx, OBJECT_P)) {
            return StatementType.COMMENT_LARGE_OBJECT;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, DATA_P) && hasToken(ctx, WRAPPER)) {
            return StatementType.COMMENT_FOREIGN_DATA_WRAPPER;
        } else if (hasToken(ctx, SERVER)) {
            return StatementType.COMMENT_FOREIGN_SERVER;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return StatementType.COMMENT_TABLE;
        } else if (hasToken(ctx, TRANSFORM)) {
            return StatementType.COMMENT_TRANSFORM;
        } else if (hasToken(ctx, LANGUAGE)) {
            return StatementType.COMMENT_LANGUAGE;
        } else if (hasToken(ctx, POLICY)) {
            return StatementType.COMMENT_POLICY;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return StatementType.ALTER_POLICY;
        } else if (hasToken(ctx, ACCESS) && hasToken(ctx, METHOD)) {
            return StatementType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, RULE) || hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return StatementType.ALTER_POLICY;
        } else if (hasToken(ctx, TRIGGER)) {
            return StatementType.COMMENT_TRIGGER;
        } else if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null || hasToken(ctx, CAST)
                   || hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return StatementType.COMMENT_PROG_OBJ;
        } else if (hasToken(ctx, ROLE)) {
            return StatementType.COMMENT_ROLE;
        } else if (hasToken(ctx, TABLESPACE)) {
            return StatementType.COMMENT_TABLESPACE;
        } else if (hasToken(ctx, INDEX)) {
            return StatementType.COMMENT_INDEX;
        } else if (hasToken(ctx, DATABASE)) {
            return StatementType.COMMENT_CATALOG;
        } else if (hasToken(ctx, SCHEMA)) {
            return StatementType.COMMENT_SCHEMA;
        } else if (hasToken(ctx, CONSTRAINT)) {
            return StatementType.COMMENT_CONSTRAINT;
        } else if (hasToken(ctx, VIEW)) {
            return StatementType.COMMENT_VIEW;
        } else if (hasToken(ctx, SEQUENCE)) {
            return StatementType.COMMENT_SEQUENCE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return StatementType.COMMENT_TYPE;
        } else if (hasToken(ctx, STATISTICS)) {
            return StatementType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, PUBLICATION) || hasToken(ctx, SUBSCRIPTION)) {
            return StatementType.ALTER_PUB_SUB;
        }
        return visitChildren(ctx);
    }

    @Override
    public StatementType visitCreateplangstmt(CreateplangstmtContext ctx) {
        return StatementType.CREATE_LANGUAGE;
    }

    @Override
    public StatementType visitCreatetransformstmt(CreatetransformstmtContext ctx) {
        return StatementType.CREATE_TRANSFORM;
    }

    @Override
    public StatementType visitDroptransformstmt(DroptransformstmtContext ctx) {
        return StatementType.DROP_TRANSFORM;
    }

    @Override
    public StatementType visitSeclabelstmt(SeclabelstmtContext ctx) {
        return StatementType.SECURITY_LABEL;
    }

    @Override
    public StatementType visitCreatedbstmt(CreatedbstmtContext ctx) {
        return StatementType.CREATE_CATALOG;
    }

    @Override
    public StatementType visitDropdbstmt(DropdbstmtContext ctx) {
        return StatementType.DROP_CATALOG;
    }

    @Override
    public StatementType visitCreateschemastmt(CreateschemastmtContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitVariableshowstmt(VariableshowstmtContext ctx) {
        return StatementType.METADATA;
    }

    @Override
    public StatementType visitVariablesetstmt(VariablesetstmtContext ctx) {
        Set_restContext set = ctx.set_rest();
        if (set.TRANSACTION() != null || set.SESSION() != null || set.set_rest_more() != null && set.set_rest_more().TRANSACTION() != null) {
            return StatementType.TRANSACTION;
        }
        Set_rest_moreContext more = set.set_rest_more();
        if (more != null && more.ROLE() != null) {
            return StatementType.SWITCH_ROLE;
        }
        if (more != null && more.SESSION() != null && more.AUTHORIZATION() != null) {
            return StatementType.SWITCH_USER;
        }
        if (more != null && more.CATALOG() != null) {
            return StatementType.SWITCH_CATALOG;
        }
        if (more != null && more.SCHEMA() != null) {
            return StatementType.SWITCH_SCHEMA;
        }
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitVariableresetstmt(VariableresetstmtContext ctx) {
        Reset_restContext reset = ctx.reset_rest();
        if (reset.TRANSACTION() != null) {
            return StatementType.TRANSACTION;
        }
        if (reset.SESSION() != null && reset.AUTHORIZATION() != null) {
            return StatementType.SWITCH_USER;
        }
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitConstraintssetstmt(ConstraintssetstmtContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitAltersystemstmt(AltersystemstmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateamstmt(CreateamstmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateconversionstmt(CreateconversionstmtContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitCreatestatsstmt(CreatestatsstmtContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitAlterstatsstmt(AlterstatsstmtContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitDropschemastmt(DropschemastmtContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitAlterownerstmt(AlterownerstmtContext ctx) {
        return StatementType.TRANSFER_PRIVILEGE;
    }

    @Override
    public StatementType visitAlterobjectschemastmt(AlterobjectschemastmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return StatementType.ALTER_POLICY;
        } else if (hasToken(ctx, SEQUENCE)) {
            return StatementType.ALTER_SEQUENCE;
        } else if (hasToken(ctx, VIEW)) {
            return StatementType.ALTER_VIEW;
        } else if (hasToken(ctx, TABLE) || hasToken(ctx, FOREIGN)) {
            return StatementType.ALTER_TABLE;
        } else if (hasToken(ctx, STATISTICS)) {
            return StatementType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, EXTENSION)) {
            return StatementType.ALTER_LIBRARY;
        } else if (hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return StatementType.ALTER_POLICY;
        } else if (hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return StatementType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return StatementType.ALTER_TYPE;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitCreatestmt(CreatestmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitCreateasstmt(CreateasstmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAltertablestmt(AltertablestmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == TABLE) {
                return StatementType.ALTER_TABLE;

            } else if (childNode.getSymbol().getType() == INDEX) {
                return StatementType.ALTER_INDEX;

            } else if (childNode.getSymbol().getType() == VIEW) {
                return StatementType.ALTER_VIEW;

            }
        }
        if (hasToken(ctx, MATERIALIZED) && hasToken(ctx, VIEW)) {
            return StatementType.ALTER_VIEW;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return StatementType.ALTER_TABLE;
        } else if (hasToken(ctx, SEQUENCE)) {
            return StatementType.ALTER_SEQUENCE;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAddColumn(AddColumnContext ctx) {
        return StatementType.ADD_COLUMN;
    }

    @Override
    public StatementType visitAlterColumn(AlterColumnContext ctx) {
        return StatementType.ALTER_COLUMN;
    }

    @Override
    public StatementType visitDropColumn(DropColumnContext ctx) {
        return StatementType.DROP_COLUMN;
    }

    @Override
    public StatementType visitAddConstraint(AddConstraintContext ctx) {
        return StatementType.ADD_CONSTRAINT;
    }

    @Override
    public StatementType visitAlterConstaint(AlterConstaintContext ctx) {
        return StatementType.ALTER_CONSTRAINT;
    }

    @Override
    public StatementType visitValidateConstraint(ValidateConstraintContext ctx) {
        return StatementType.ALTER_CONSTRAINT;
    }

    @Override
    public StatementType visitDropConstraint(DropConstraintContext ctx) {
        return StatementType.DROP_CONSTRAINT;
    }

    @Override
    public StatementType visitPartition_cmd(Partition_cmdContext ctx) {
        return StatementType.ALTER_PARTITION;
    }

    @Override
    public StatementType visitDroptablestmt(DroptablestmtContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitDropstmt(DropstmtContext ctx) {
        if (hasToken(ctx, EXTENSION)) {
            return StatementType.DROP_LIBRARY;
        } else if (hasToken(ctx, LANGUAGE)) {
            return StatementType.DROP_LANGUAGE;
        } else if (hasToken(ctx, INDEX)) {
            return StatementType.DROP_INDEX;
        } else if (hasToken(ctx, VIEW)) {
            return StatementType.DROP_VIEW;
        } else if (hasToken(ctx, TRIGGER) && !hasToken(ctx, EVENT)) {
            return StatementType.DROP_TRIGGER;
        } else if (hasToken(ctx, POLICY)) {
            return StatementType.DROP_POLICY;
        } else if (hasToken(ctx, RULE)) {
            return StatementType.DROP_POLICY;
        } else if (hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return StatementType.DROP_POLICY;
        } else if (hasToken(ctx, PUBLICATION)) {
            return StatementType.DROP_PUB_SUB;
        } else if (hasToken(ctx, SEQUENCE)) {
            return StatementType.DROP_SEQUENCE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return StatementType.DROP_TYPE;
        } else if (hasToken(ctx, EVENT) && hasToken(ctx, TRIGGER)) {
            return StatementType.DROP_TRIGGER;
        } else if (hasToken(ctx, TABLESPACE)) {
            return StatementType.DROP_TABLESPACE;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return StatementType.DROP_TABLE;
        } else if (hasToken(ctx, STATISTICS)) {
            return StatementType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, ACCESS) && hasToken(ctx, METHOD)) {
            return StatementType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return StatementType.DROP_POLICY;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, WRAPPER) || hasToken(ctx, SERVER)) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        return StatementType.UNKNOWN;
    }

    private boolean hasToken(ParseTree tree, int type) {
        if (tree instanceof TerminalNodeImpl childNode) {
            return childNode.getSymbol().getType() == type;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (hasToken(tree.getChild(i), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StatementType visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        return StatementType.CREATE_TRIGGER;
    }

    @Override
    public StatementType visitRenamestmt(RenamestmtContext ctx) {
        if (ctx.rename_column_stmt() != null) {
            return StatementType.RENAME_COLUMN;
        } else if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || hasToken(ctx, LANGUAGE)) {
            return StatementType.RENAME_PROG_OBJ;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return StatementType.ALTER_POLICY;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, WRAPPER) || hasToken(ctx, SERVER)) {
            return StatementType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, RULE) || hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return StatementType.ALTER_POLICY;
        } else if (hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return StatementType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TRIGGER)) {
            return StatementType.RENAME_TRIGGER;
        } else if (hasToken(ctx, INDEX)) {
            return StatementType.RENAME_INDEX;
        } else if (hasToken(ctx, USER)) {
            return StatementType.RENAME_USER;
        } else if (hasToken(ctx, ROLE) || hasToken(ctx, GROUP_P)) {
            return StatementType.RENAME_ROLE;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return StatementType.RENAME_TABLE;
        } else if (hasToken(ctx, VIEW) && hasToken(ctx, COLUMN)) {
            return StatementType.RENAME_COLUMN;
        } else if (hasToken(ctx, VIEW)) {
            return StatementType.RENAME_VIEW;
        } else if (hasToken(ctx, PUBLICATION)) {
            return StatementType.ALTER_PUB_SUB;
        } else if (hasToken(ctx, SUBSCRIPTION)) {
            return StatementType.ALTER_PUB_SUB;
        } else if (hasToken(ctx, SEQUENCE)) {
            return StatementType.RENAME_SEQUENCE;
        } else if (hasToken(ctx, TABLESPACE)) {
            return StatementType.RENAME_TABLESPACE;
        } else if (hasToken(ctx, STATISTICS)) {
            return StatementType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, CONSTRAINT)) {
            return StatementType.RENAME_CONSTRAINT;
        } else if (hasToken(ctx, DOMAIN_P) && hasToken(ctx, CONSTRAINT)) {
            return StatementType.RENAME_CONSTRAINT;
        } else if (hasToken(ctx, TYPE_P) && hasToken(ctx, ATTRIBUTE)) {
            return StatementType.ALTER_TYPE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return StatementType.RENAME_TYPE;
        }
        return visitChildren(ctx);
    }

    @Override
    public StatementType visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        return StatementType.CREATE_PUB_SUB;
    }

    @Override
    public StatementType visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        return StatementType.ALTER_PUB_SUB;
    }

    @Override
    public StatementType visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        return StatementType.CREATE_PUB_SUB;
    }

    @Override
    public StatementType visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        if (ctx.ENABLE_P() != null || ctx.DISABLE_P() != null || ctx.REFRESH() != null || ctx.SKIP_P() != null) {
            return StatementType.ADMIN_PUB_SUB;
        }
        return StatementType.ALTER_PUB_SUB;
    }

    @Override
    public StatementType visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        return StatementType.DROP_PUB_SUB;
    }

    @Override
    public StatementType visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        if (ctx.function_with_argtypes() != null) {
            return StatementType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TRIGGER)) {
            return StatementType.ALTER_TRIGGER;
        } else if (hasToken(ctx, INDEX)) {
            return StatementType.ALTER_INDEX;
        } else if (hasToken(ctx, MATERIALIZED)) {
            return StatementType.ALTER_VIEW;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitIndexstmt(IndexstmtContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitViewstmt(ViewstmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreatecaststmt(CreatecaststmtContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateopclassstmt(CreateopclassstmtContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitCreateopfamilystmt(CreateopfamilystmtContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitAlteropfamilystmt(AlteropfamilystmtContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitDropcaststmt(DropcaststmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitDropopclassstmt(DropopclassstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitDropopfamilystmt(DropopfamilystmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitAlteroperatorstmt(AlteroperatorstmtContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitDefinestmt(DefinestmtContext ctx) {
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return StatementType.CREATE_POLICY;
        } else if (ctx.AGGREGATE() != null || ctx.OPERATOR() != null) {
            return StatementType.CREATE_PROG_OBJ;
        } else if (hasToken(ctx, COLLATION)) {
            return StatementType.CREATE_POLICY;
        } else if (ctx.TYPE_P() != null) {
            return StatementType.CREATE_TYPE;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAltercollationstmt(AltercollationstmtContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitAltertsdictionarystmt(AltertsdictionarystmtContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitAltertsconfigurationstmt(AltertsconfigurationstmtContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitSelectstmt(SelectstmtContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitSelect_no_parens(Select_no_parensContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsertstmt(InsertstmtContext ctx) {
        return hasToken(ctx, CONFLICT) ? StatementType.MERGE : StatementType.INSERT;
    }

    @Override
    public StatementType visitMergestmt(MergestmtContext ctx) {
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitUpdatestmt(UpdatestmtContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDeletestmt(DeletestmtContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCreateuserstmt(CreateuserstmtContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitDropuserstmt(DropuserstmtContext ctx) {
        return StatementType.DROP_USER;
    }

    @Override
    public StatementType visitAlterrolestmt(AlterrolestmtContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitAlterrolesetstmt(AlterrolesetstmtContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitCreaterolestmt(CreaterolestmtContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitDroprolestmt(DroprolestmtContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitCreategroupstmt(CreategroupstmtContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitAltergroupstmt(AltergroupstmtContext ctx) {
        return StatementType.ALTER_USER;
    }

    @Override
    public StatementType visitDropgroupstmt(DropgroupstmtContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitRemoveaggrstmt(RemoveaggrstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitRemoveoperstmt(RemoveoperstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitGrantstmt(GrantstmtContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokestmt(RevokestmtContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitGrantrolestmt(GrantrolestmtContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokerolestmt(RevokerolestmtContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitReassignownedstmt(ReassignownedstmtContext ctx) {
        return StatementType.TRANSFER_PRIVILEGE;
    }

    @Override
    public StatementType visitDropownedstmt(DropownedstmtContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitListenstmt(ListenstmtContext ctx) {
        return StatementType.ADMIN_PUB_SUB;
    }

    @Override
    public StatementType visitUnlistenstmt(UnlistenstmtContext ctx) {
        return StatementType.ADMIN_PUB_SUB;
    }

    @Override
    public StatementType visitNotifystmt(NotifystmtContext ctx) {
        return StatementType.ADMIN_PUB_SUB;
    }

    @Override
    public StatementType visitAlterdefaultprivilegesstmt(AlterdefaultprivilegesstmtContext ctx) {
        return hasToken(ctx.defaclaction(), GRANT) ? StatementType.GRANT : StatementType.REVOKE;
    }

    @Override
    public StatementType visitCallstmt(CallstmtContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitFor_locking_clause(For_locking_clauseContext ctx) {
        return StatementType.QUERY_LOCK;
    }

    @Override
    public StatementType visitLockstmt(LockstmtContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitFetchstmt(FetchstmtContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCloseportalstmt(CloseportalstmtContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitDeclarecursorstmt(DeclarecursorstmtContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitRulestmt(RulestmtContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitCopystmt(CopystmtContext ctx) {
        return ctx.copy_from() != null && ctx.copy_from().FROM() != null ? StatementType.DATA_IMPORT : StatementType.DATA_EXPORT;
    }

    @Override
    public StatementType visitProgram_(Program_Context ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitExplainstmt(ExplainstmtContext ctx) {
        if (isExplainAnalyze(ctx)) {
            return ctx.explainablestmt().accept(this);
        }
        return StatementType.PERFORMANCE;
    }

    private boolean isExplainAnalyze(ExplainstmtContext ctx) {
        if (ctx.analyze_keyword() != null) {
            return true;
        }
        if (ctx.explain_option_list() == null) {
            return false;
        }
        for (Explain_option_elemContext option : ctx.explain_option_list().explain_option_elem()) {
            if (option.explain_option_name().analyze_keyword() == null) {
                continue;
            }
            if (option.explain_option_arg() == null) {
                return true;
            }
            String value = option.explain_option_arg().getText();
            return !("false".equalsIgnoreCase(value) || "off".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "0".equals(value));
        }
        return false;
    }

    @Override
    public StatementType visitTransactionstmt(TransactionstmtContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitLoadstmt(LoadstmtContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitCreateextensionstmt(CreateextensionstmtContext ctx) {
        return StatementType.CREATE_LIBRARY;
    }

    @Override
    public StatementType visitAlterextensionstmt(AlterextensionstmtContext ctx) {
        return StatementType.ALTER_LIBRARY;
    }

    @Override
    public StatementType visitAlterextensioncontentsstmt(AlterextensioncontentsstmtContext ctx) {
        return StatementType.ALTER_LIBRARY;
    }

    @Override
    public StatementType visitVacuumstmt(VacuumstmtContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitClusterstmt(ClusterstmtContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitReindexstmt(ReindexstmtContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitCheckpointstmt(CheckpointstmtContext ctx) {
        return StatementType.MAINTAIN_LOG;
    }

    @Override
    public StatementType visitDiscardstmt(DiscardstmtContext ctx) {
        if (ctx.PLANS() != null) {
            return StatementType.ADMIN_PERFORMANCE;
        } else if (ctx.TEMP() != null || ctx.TEMPORARY() != null) {
            return StatementType.DROP_TABLE;
        }
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreatetablespacestmt(CreatetablespacestmtContext ctx) {
        return StatementType.CREATE_TABLESPACE;
    }

    @Override
    public StatementType visitAltertblspcstmt(AltertblspcstmtContext ctx) {
        return StatementType.ALTER_TABLESPACE;
    }

    @Override
    public StatementType visitDroptablespacestmt(DroptablespacestmtContext ctx) {
        return StatementType.DROP_TABLESPACE;
    }

    @Override
    public StatementType visitAlterseqstmt(AlterseqstmtContext ctx) {
        return StatementType.ALTER_SEQUENCE;
    }

    @Override
    public StatementType visitCreatedomainstmt(CreatedomainstmtContext ctx) {
        return StatementType.CREATE_TYPE;
    }

    @Override
    public StatementType visitAlterdomainstmt(AlterdomainstmtContext ctx) {
        return StatementType.ALTER_TYPE;
    }

    @Override
    public StatementType visitAltercompositetypestmt(AltercompositetypestmtContext ctx) {
        return StatementType.ALTER_TYPE;
    }

    @Override
    public StatementType visitAlterenumstmt(AlterenumstmtContext ctx) {
        return StatementType.ALTER_TYPE;
    }

    @Override
    public StatementType visitAltertypestmt(AltertypestmtContext ctx) {
        return StatementType.ALTER_TYPE;
    }

    @Override
    public StatementType visitCreateforeigntablestmt(CreateforeigntablestmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitCreatefdwstmt(CreatefdwstmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterfdwstmt(AlterfdwstmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateforeignserverstmt(CreateforeignserverstmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAlterforeignserverstmt(AlterforeignserverstmtContext ctx) {
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitCreateusermappingstmt(CreateusermappingstmtContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitAlterusermappingstmt(AlterusermappingstmtContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitDropusermappingstmt(DropusermappingstmtContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitImportforeignschemastmt(ImportforeignschemastmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitCreateeventtrigstmt(CreateeventtrigstmtContext ctx) {
        return StatementType.CREATE_TRIGGER;
    }

    @Override
    public StatementType visitAltereventtrigstmt(AltereventtrigstmtContext ctx) {
        return StatementType.ALTER_TRIGGER;
    }

    @Override
    public StatementType visitPreparestmt(PreparestmtContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitExecutestmt(ExecutestmtContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitDeallocatestmt(DeallocatestmtContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitChildren(RuleNode node) {
        if (this.currentNodeOnly) {
            return null;
        }
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            StatementType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }
        return StatementType.UNKNOWN;
    }

    private void collectSystemFunctionTypes(Func_applicationContext ctx) {
        String name = normalizeFunctionName(ctx.func_name());
        if (isMetadataFunction(name)) {
            this.metadataReference = true;
            this.types.add(StatementType.METADATA);
            return;
        }
        int previousSize = this.types.size();
        switch (name) {
            case "set_config" -> this.types.add(StatementType.SESSION_SETTING_WRITE);
            case "pg_advisory_lock", "pg_advisory_lock_shared", "pg_advisory_unlock", "pg_advisory_unlock_all", "pg_advisory_unlock_shared", "pg_advisory_xact_lock",
                    "pg_advisory_xact_lock_shared", "pg_try_advisory_lock", "pg_try_advisory_lock_shared", "pg_try_advisory_xact_lock", "pg_try_advisory_xact_lock_shared" ->
                this.types.add(StatementType.SESSION_LOCK);
            case "pg_current_wal_flush_lsn", "pg_current_wal_insert_lsn", "pg_current_wal_lsn", "pg_last_wal_receive_lsn", "pg_last_wal_replay_lsn", "pg_walfile_name",
                    "pg_walfile_name_offset", "pg_wal_lsn_diff", "pg_get_wal_replay_pause_state", "pg_is_wal_replay_paused", "pg_last_xact_replay_timestamp" ->
                this.types.add(StatementType.LOG_READ);
            case "pg_create_restore_point", "pg_switch_wal", "pg_rotate_logfile" -> this.types.add(StatementType.MAINTAIN_LOG);
            case "pg_read_file", "pg_read_binary_file" -> {
                this.types.add(StatementType.DATA_IMPORT);
                this.types.add(StatementType.UNSAFE);
            }
            case "pg_ls_dir", "pg_stat_file" -> {
                this.types.add(StatementType.DATA_IMPORT);
                this.types.add(StatementType.UNSAFE);
            }
            case "pg_ls_tmpdir" -> {
                this.types.add(StatementType.DATA_IMPORT);
                this.types.add(StatementType.UNSAFE);
            }
            case "pg_ls_logdir", "pg_ls_waldir", "pg_ls_archive_statusdir", "pg_current_logfile", "pg_control_checkpoint", "pg_control_init", "pg_control_recovery",
                    "pg_control_system" ->
                this.types.add(StatementType.LOG_READ);
            case "pg_import_system_collations", "pg_reload_conf" -> this.types.add(StatementType.SYSTEM_SETTING_WRITE);
            case "pg_promote" -> this.types.add(StatementType.ALTER_REPLICATION);
            case "pg_wal_replay_pause", "pg_wal_replay_resume", "pg_sync_replication_slots", "pg_log_standby_snapshot", "pg_replication_slot_advance", "pg_logical_emit_message" ->
                this.types.add(StatementType.ADMIN_REPLICATION);
            case "pg_create_physical_replication_slot", "pg_create_logical_replication_slot", "pg_copy_physical_replication_slot", "pg_copy_logical_replication_slot",
                    "pg_replication_origin_create" ->
                this.types.add(StatementType.CREATE_REPLICATION);
            case "pg_drop_replication_slot", "pg_replication_origin_drop" -> this.types.add(StatementType.DROP_REPLICATION);
            case "pg_replication_origin_advance", "pg_replication_origin_session_setup", "pg_replication_origin_session_reset", "pg_replication_origin_xact_setup",
                    "pg_replication_origin_xact_reset" ->
                this.types.add(StatementType.ALTER_REPLICATION);
            case "pg_replication_origin_oid", "pg_replication_origin_progress", "pg_replication_origin_session_is_setup", "pg_replication_origin_session_progress" ->
                this.types.add(StatementType.METADATA);
            case "pg_logical_slot_peek_changes", "pg_logical_slot_peek_binary_changes" -> this.types.add(StatementType.LOG_READ);
            case "pg_logical_slot_get_changes", "pg_logical_slot_get_binary_changes" -> {
                this.types.add(StatementType.LOG_READ);
                this.types.add(StatementType.ADMIN_REPLICATION);
            }
            case "pg_cancel_backend", "pg_terminate_backend", "pg_backup_start", "pg_backup_stop" -> this.types.add(StatementType.ADMIN);
            case "pg_log_backend_memory_contexts" -> this.types.add(StatementType.ADMIN_LOG);
            case "brin_desummarize_range", "brin_summarize_new_values", "brin_summarize_range", "gin_clean_pending_list" -> this.types.add(StatementType.ADMIN_PERFORMANCE);
            case "pg_blocking_pids", "pg_safe_snapshot_blocking_pids", "pg_notification_queue_usage", "pg_mcv_list_items" -> this.types.add(StatementType.PERFORMANCE);
            case "pg_restore_relation_stats", "pg_clear_relation_stats", "pg_restore_attribute_stats", "pg_clear_attribute_stats" -> {
                if (this.version.atLeast(PostgresVersion.POSTGRES_18)) {
                    this.types.add(StatementType.ADMIN_PERFORMANCE);
                }
            }
            case "pg_available_wal_summaries", "pg_wal_summary_contents" -> {
                if (this.version.atLeast(PostgresVersion.POSTGRES_17)) {
                    this.types.add(StatementType.LOG_READ);
                }
            }
            case "pg_ls_summariesdir" -> {
                if (this.version.atLeast(PostgresVersion.POSTGRES_18)) {
                    this.types.add(StatementType.LOG_READ);
                }
            }
            default -> {
            }
        }
        if (this.types.size() > previousSize) {
            this.requiresSelectCarrier = true;
        }
    }

    private boolean isMetadataFunction(String name) {
        if (METADATA_FUNCTIONS.contains(name)) {
            return true;
        }
        if (name.equals("pg_database_collation_actual_version") || name.equals("pg_get_wal_resource_managers")) {
            return this.version.atLeast(PostgresVersion.POSTGRES_15);
        }
        if (name.equals("pg_column_toast_chunk_id")) {
            return this.version.atLeast(PostgresVersion.POSTGRES_17);
        }
        return name.equals("pg_settings_get_flags") && this.version.atLeast(PostgresVersion.POSTGRES_18);
    }

    private void collectRelationType(Relation_exprContext relation) {
        Qualified_nameContext name = relation.qualified_name();
        String unqualifiedName = normalizeQualifiedName(name);
        if (PERFORMANCE_RELATIONS.contains(unqualifiedName)) {
            this.requiresSelectCarrier = true;
            this.types.add(StatementType.PERFORMANCE);
        } else if (isSystemRelation(name)) {
            this.metadataReference = true;
            this.types.add(StatementType.METADATA);
        } else {
            this.ordinaryRelation = true;
        }
    }

    private String normalizeFunctionName(Func_nameContext name) {
        if (name.indirection() != null) {
            List<Indirection_elContext> elements = name.indirection().indirection_el();
            Indirection_elContext last = elements.get(elements.size() - 1);
            if (last.attr_name() != null) {
                return normalizeName(last.attr_name().getText());
            }
        }
        if (name.type_function_name() != null) {
            return normalizeName(name.type_function_name().getText());
        }
        return normalizeName(name.colid().getText());
    }

    private String normalizeQualifiedName(Qualified_nameContext name) {
        if (name.indirection() != null) {
            List<Indirection_elContext> elements = name.indirection().indirection_el();
            Indirection_elContext last = elements.get(elements.size() - 1);
            if (last.attr_name() != null) {
                return normalizeName(last.attr_name().getText());
            }
        }
        return normalizeName(name.colid().getText());
    }

    private boolean isSystemRelation(Qualified_nameContext name) {
        String schema = normalizeName(name.colid().getText());
        if (name.indirection() == null) {
            return false;
        }
        List<Indirection_elContext> elements = name.indirection().indirection_el();
        Indirection_elContext last = elements.get(elements.size() - 1);
        return last.attr_name() != null && RESOURCES.isSystemRelation(schema, normalizeName(last.attr_name().getText()), this.version);
    }

    private String normalizeName(String name) {
        return name.replace("\"", "").toLowerCase(Locale.ROOT);
    }

}
