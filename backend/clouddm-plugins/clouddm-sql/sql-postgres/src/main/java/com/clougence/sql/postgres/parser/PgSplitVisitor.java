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
package com.clougence.sql.postgres.parser;

import static com.clougence.sql.postgres.parser.antlr.PgSqlParser.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.postgres.parser.antlr.PgSqlParserBaseVisitor;

public class PgSplitVisitor extends PgSqlParserBaseVisitor<SplitQueryType> {

    private static final Set<String>  METADATA_FUNCTIONS    = Set
        .of("acldefault", "aclexplode", "col_description", "format_type", "has_any_column_privilege", "has_column_privilege", "has_database_privilege", "has_foreign_data_wrapper_privilege", "has_function_privilege", "has_language_privilege", "has_largeobject_privilege", "has_parameter_privilege", "has_schema_privilege", "has_sequence_privilege", "has_server_privilege", "has_table_privilege", "has_tablespace_privilege", "has_type_privilege", "makeaclitem", "obj_description", "pg_char_to_encoding", "pg_collation_actual_version", "pg_collation_is_visible", "pg_column_compression", "pg_column_size", "pg_conversion_is_visible", "pg_database_size", "pg_describe_object", "pg_encoding_to_char", "pg_filenode_relation", "pg_function_is_visible", "pg_get_catalog_foreign_keys", "pg_get_constraintdef", "pg_get_expr", "pg_get_function_arguments", "pg_get_function_identity_arguments", "pg_get_function_result", "pg_get_functiondef", "pg_get_indexdef", "pg_get_keywords", "pg_get_object_address", "pg_get_partition_constraintdef", "pg_get_partkeydef", "pg_get_ruledef", "pg_get_serial_sequence", "pg_get_statisticsobjdef", "pg_get_triggerdef", "pg_get_userbyid", "pg_get_viewdef", "pg_has_role", "pg_identify_object", "pg_identify_object_as_address", "pg_index_column_has_property", "pg_index_has_property", "pg_indexam_has_property", "pg_indexes_size", "pg_is_in_recovery", "pg_listening_channels", "pg_opclass_is_visible", "pg_operator_is_visible", "pg_opfamily_is_visible", "pg_partition_ancestors", "pg_partition_root", "pg_partition_tree", "pg_relation_filenode", "pg_relation_filepath", "pg_relation_size", "pg_statistics_obj_is_visible", "pg_table_is_visible", "pg_table_size", "pg_tablespace_databases", "pg_tablespace_location", "pg_tablespace_size", "pg_total_relation_size", "pg_ts_config_is_visible", "pg_ts_dict_is_visible", "pg_ts_parser_is_visible", "pg_ts_template_is_visible", "pg_type_is_visible", "pg_typeof", "row_security_active", "shobj_description", "to_regclass", "to_regcollation", "to_regnamespace", "to_regoper", "to_regoperator", "to_regproc", "to_regprocedure", "to_regrole", "to_regtype");

    private static final Set<String>  PERFORMANCE_RELATIONS = Set.of("pg_statistic", "pg_statistic_ext", "pg_statistic_ext_data", "pg_stats", "pg_stats_ext", "pg_stats_ext_exprs");

    private final PostgresVersion     version;
    private final Set<SplitQueryType> types                 = new LinkedHashSet<>();
    private boolean                   metadataReference;
    private boolean                   ordinaryRelation;
    private boolean                   requiresSelectCarrier;
    private boolean                   currentNodeOnly;

    public PgSplitVisitor(){
        this(PostgresVersion.LATEST);
    }

    public PgSplitVisitor(PostgresVersion version){
        this.version = version == null ? PostgresVersion.LATEST : version;
    }

    @Override
    public SplitQueryType visit(ParseTree tree) {
        collectTypes(tree);
        return this.types.stream().findFirst().orElse(null);
    }

    public Set<SplitQueryType> collectTypes(ParseTree tree) {
        this.types.clear();
        this.metadataReference = false;
        this.ordinaryRelation = false;
        this.requiresSelectCarrier = false;
        collectNode(tree);
        if (this.metadataReference && !this.ordinaryRelation && !this.requiresSelectCarrier) {
            this.types.remove(SplitQueryType.SELECT);
        }
        return new LinkedHashSet<>(this.types);
    }

    private void collectNode(ParseTree tree) {
        boolean previous = this.currentNodeOnly;
        SplitQueryType type;
        if (isOwnershipTransfer(tree)) {
            type = SplitQueryType.TRANSFER_PRIVILEGE;
        } else {
            try {
                this.currentNodeOnly = true;
                type = tree.accept(this);
            } finally {
                this.currentNodeOnly = previous;
            }
        }
        if (tree instanceof ExecutestmtContext && hasToken(tree, CREATE)) {
            this.types.add(SplitQueryType.CREATE_TABLE);
        }
        if (type != null) {
            this.types.add(type);
        }
        if (tree instanceof DeclarecursorstmtContext || tree instanceof FetchstmtContext || tree instanceof CloseportalstmtContext) {
            this.types.add(SplitQueryType.PROGRAM_CONTROL);
        }
        if (tree instanceof VariablesetstmtContext ctx && ctx.set_rest().SESSION() != null) {
            this.types.add(SplitQueryType.SESSION_SETTING_WRITE);
        }
        if (tree instanceof Func_applicationContext function) {
            collectSystemFunctionTypes(function);
        }
        if (tree instanceof Relation_exprContext relation) {
            collectRelationType(relation);
        }
        if (tree instanceof Func_expr_common_subexprContext expression && hasToken(expression, COLLATION) && hasToken(expression, FOR)) {
            this.metadataReference = true;
            this.types.add(SplitQueryType.METADATA);
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

    private boolean shouldDescend(ParseTree tree, SplitQueryType type) {
        if (tree instanceof ExplainstmtContext ctx) {
            return isExplainAnalyze(ctx);
        }
        if (tree instanceof CreateasstmtContext || tree instanceof SelectstmtContext || tree instanceof Select_no_parensContext || tree instanceof InsertstmtContext
            || tree instanceof UpdatestmtContext || tree instanceof DeletestmtContext || tree instanceof MergestmtContext || tree instanceof CopystmtContext
            || tree instanceof AltertablestmtContext || tree instanceof DeclarecursorstmtContext) {
            return true;
        }
        return type == null;
    }

    @Override
    public SplitQueryType visitDostmt(DostmtContext ctx) {
        return SplitQueryType.BLOCK;
    }

    @Override
    public SplitQueryType visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitAnalyzestmt(AnalyzestmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitCreateseqstmt(CreateseqstmtContext ctx) {
        return SplitQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitTruncatestmt(TruncatestmtContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        return SplitQueryType.ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        return SplitQueryType.ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitRename_table_stmt(Rename_table_stmtContext ctx) {
        return SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitRename_database_stmt(Rename_database_stmtContext ctx) {
        return SplitQueryType.RENAME_CATALOG;
    }

    @Override
    public SplitQueryType visitRename_column_stmt(Rename_column_stmtContext ctx) {
        return SplitQueryType.RENAME_COLUMN;
    }

    @Override
    public SplitQueryType visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        return SplitQueryType.RENAME_SCHEMA;
    }

    @Override
    public SplitQueryType visitComment_table_stmt(Comment_table_stmtContext ctx) {
        return SplitQueryType.COMMENT_TABLE;
    }

    @Override
    public SplitQueryType visitComment_column_stmt(Comment_column_stmtContext ctx) {
        return SplitQueryType.COMMENT_COLUMN;
    }

    @Override
    public SplitQueryType visitCommentstmt(CommentstmtContext ctx) {
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SplitQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, ACCESS) && hasToken(ctx, METHOD)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, RULE) || hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SplitQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, TRIGGER)) {
            return SplitQueryType.COMMENT_TRIGGER;
        } else if (ctx.aggregate_with_argtypes() != null || ctx.operator_with_argtypes() != null || hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return SplitQueryType.COMMENT_PROG_OBJ;
        } else if (hasToken(ctx, ROLE)) {
            return SplitQueryType.COMMENT_ROLE;
        } else if (hasToken(ctx, TABLESPACE)) {
            return SplitQueryType.COMMENT_TABLESPACE;
        } else if (hasToken(ctx, INDEX)) {
            return SplitQueryType.COMMENT_INDEX;
        } else if (hasToken(ctx, DATABASE)) {
            return SplitQueryType.COMMENT_CATALOG;
        } else if (hasToken(ctx, SCHEMA)) {
            return SplitQueryType.COMMENT_SCHEMA;
        } else if (hasToken(ctx, CONSTRAINT)) {
            return SplitQueryType.COMMENT_CONSTRAINT;
        } else if (hasToken(ctx, VIEW)) {
            return SplitQueryType.COMMENT_VIEW;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SplitQueryType.COMMENT_SEQUENCE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, PUBLICATION) || hasToken(ctx, SUBSCRIPTION)) {
            return SplitQueryType.ALTER_PUB_SUB;
        }
        return visitChildren(ctx);
    }

    @Override
    public SplitQueryType visitCreatedbstmt(CreatedbstmtContext ctx) {
        return SplitQueryType.CREATE_CATALOG;
    }

    @Override
    public SplitQueryType visitDropdbstmt(DropdbstmtContext ctx) {
        return SplitQueryType.DROP_CATALOG;
    }

    @Override
    public SplitQueryType visitCreateschemastmt(CreateschemastmtContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitVariableshowstmt(VariableshowstmtContext ctx) {
        return SplitQueryType.METADATA;
    }

    @Override
    public SplitQueryType visitVariablesetstmt(VariablesetstmtContext ctx) {
        Set_restContext set = ctx.set_rest();
        if (set.TRANSACTION() != null || set.SESSION() != null || set.set_rest_more() != null && set.set_rest_more().TRANSACTION() != null) {
            return SplitQueryType.TRANSACTION;
        }
        Set_rest_moreContext more = set.set_rest_more();
        if (more != null && more.ROLE() != null) {
            return SplitQueryType.SWITCH_ROLE;
        }
        if (more != null && more.SESSION() != null && more.AUTHORIZATION() != null) {
            return SplitQueryType.SWITCH_USER;
        }
        if (more != null && more.CATALOG() != null) {
            return SplitQueryType.SWITCH_CATALOG;
        }
        if (more != null && more.SCHEMA() != null) {
            return SplitQueryType.SWITCH_SCHEMA;
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitVariableresetstmt(VariableresetstmtContext ctx) {
        Reset_restContext reset = ctx.reset_rest();
        if (reset.TRANSACTION() != null) {
            return SplitQueryType.TRANSACTION;
        }
        if (reset.SESSION() != null && reset.AUTHORIZATION() != null) {
            return SplitQueryType.SWITCH_USER;
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitConstraintssetstmt(ConstraintssetstmtContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitAltersystemstmt(AltersystemstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateamstmt(CreateamstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateconversionstmt(CreateconversionstmtContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitCreatestatsstmt(CreatestatsstmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAlterstatsstmt(AlterstatsstmtContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitDropschemastmt(DropschemastmtContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterownerstmt(AlterownerstmtContext ctx) {
        return SplitQueryType.TRANSFER_PRIVILEGE;
    }

    @Override
    public SplitQueryType visitAlterobjectschemastmt(AlterobjectschemastmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SplitQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SplitQueryType.ALTER_SEQUENCE;
        } else if (hasToken(ctx, VIEW)) {
            return SplitQueryType.ALTER_VIEW;
        } else if (hasToken(ctx, TABLE) || hasToken(ctx, FOREIGN)) {
            return SplitQueryType.ALTER_TABLE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, EXTENSION)) {
            return SplitQueryType.ALTER_LIBRARY;
        } else if (hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SplitQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return SplitQueryType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return SplitQueryType.ALTER_TYPE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitCreatestmt(CreatestmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreateasstmt(CreateasstmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAltertablestmt(AltertablestmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == TABLE) {
                return SplitQueryType.ALTER_TABLE;

            } else if (childNode.getSymbol().getType() == INDEX) {
                return SplitQueryType.ALTER_INDEX;

            } else if (childNode.getSymbol().getType() == VIEW) {
                return SplitQueryType.ALTER_VIEW;

            }
        }
        if (hasToken(ctx, MATERIALIZED) && hasToken(ctx, VIEW)) {
            return SplitQueryType.ALTER_VIEW;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return SplitQueryType.ALTER_TABLE;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SplitQueryType.ALTER_SEQUENCE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAddColumn(AddColumnContext ctx) {
        return SplitQueryType.ADD_COLUMN;
    }

    @Override
    public SplitQueryType visitAlterColumn(AlterColumnContext ctx) {
        return SplitQueryType.ALTER_COLUMN;
    }

    @Override
    public SplitQueryType visitDropColumn(DropColumnContext ctx) {
        return SplitQueryType.DROP_COLUMN;
    }

    @Override
    public SplitQueryType visitAddConstraint(AddConstraintContext ctx) {
        return SplitQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitAlterConstaint(AlterConstaintContext ctx) {
        return SplitQueryType.ALTER_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitValidateConstraint(ValidateConstraintContext ctx) {
        return SplitQueryType.ALTER_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitDropConstraint(DropConstraintContext ctx) {
        return SplitQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SplitQueryType visitPartition_cmd(Partition_cmdContext ctx) {
        return SplitQueryType.ALTER_PARTITION;
    }

    @Override
    public SplitQueryType visitDroptablestmt(DroptablestmtContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitDropstmt(DropstmtContext ctx) {
        if (hasToken(ctx, EXTENSION)) {
            return SplitQueryType.DROP_LIBRARY;
        } else if (hasToken(ctx, INDEX)) {
            return SplitQueryType.DROP_INDEX;
        } else if (hasToken(ctx, VIEW)) {
            return SplitQueryType.DROP_VIEW;
        } else if (hasToken(ctx, TRIGGER) && !hasToken(ctx, EVENT)) {
            return SplitQueryType.DROP_TRIGGER;
        } else if (hasToken(ctx, POLICY)) {
            return SplitQueryType.DROP_POLICY;
        } else if (hasToken(ctx, RULE)) {
            return SplitQueryType.DROP_POLICY;
        } else if (hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SplitQueryType.DROP_POLICY;
        } else if (hasToken(ctx, PUBLICATION)) {
            return SplitQueryType.DROP_PUB_SUB;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SplitQueryType.DROP_SEQUENCE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return SplitQueryType.DROP_TYPE;
        } else if (hasToken(ctx, EVENT) && hasToken(ctx, TRIGGER)) {
            return SplitQueryType.DROP_TRIGGER;
        } else if (hasToken(ctx, TABLESPACE)) {
            return SplitQueryType.DROP_TABLESPACE;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return SplitQueryType.DROP_TABLE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, ACCESS) && hasToken(ctx, METHOD)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SplitQueryType.DROP_POLICY;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, WRAPPER) || hasToken(ctx, SERVER)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return SplitQueryType.UNKNOWN;
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
    public SplitQueryType visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        return SplitQueryType.CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitRenamestmt(RenamestmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null) {
            return SplitQueryType.RENAME_PROG_OBJ;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SplitQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, WRAPPER) || hasToken(ctx, SERVER)) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, RULE) || hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SplitQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return SplitQueryType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TRIGGER)) {
            return SplitQueryType.RENAME_TRIGGER;
        } else if (hasToken(ctx, USER)) {
            return SplitQueryType.RENAME_USER;
        } else if (hasToken(ctx, ROLE) || hasToken(ctx, GROUP_P)) {
            return SplitQueryType.RENAME_ROLE;
        } else if (hasToken(ctx, VIEW) && hasToken(ctx, COLUMN)) {
            return SplitQueryType.RENAME_COLUMN;
        } else if (hasToken(ctx, VIEW)) {
            return SplitQueryType.RENAME_VIEW;
        } else if (hasToken(ctx, PUBLICATION)) {
            return SplitQueryType.ALTER_PUB_SUB;
        } else if (hasToken(ctx, SUBSCRIPTION)) {
            return SplitQueryType.ALTER_PUB_SUB;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SplitQueryType.RENAME_SEQUENCE;
        } else if (hasToken(ctx, TABLESPACE)) {
            return SplitQueryType.RENAME_TABLESPACE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, DOMAIN_P) && hasToken(ctx, CONSTRAINT)) {
            return SplitQueryType.RENAME_CONSTRAINT;
        } else if (hasToken(ctx, TYPE_P) && hasToken(ctx, ATTRIBUTE)) {
            return SplitQueryType.ALTER_TYPE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return SplitQueryType.RENAME_TYPE;
        }
        return visitChildren(ctx);
    }

    @Override
    public SplitQueryType visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        return SplitQueryType.CREATE_PUB_SUB;
    }

    @Override
    public SplitQueryType visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        return SplitQueryType.ALTER_PUB_SUB;
    }

    @Override
    public SplitQueryType visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        return SplitQueryType.CREATE_PUB_SUB;
    }

    @Override
    public SplitQueryType visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        if (ctx.ENABLE_P() != null || ctx.DISABLE_P() != null || ctx.REFRESH() != null || ctx.SKIP_P() != null) {
            return SplitQueryType.ADMIN_PUB_SUB;
        }
        return SplitQueryType.ALTER_PUB_SUB;
    }

    @Override
    public SplitQueryType visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        return SplitQueryType.DROP_PUB_SUB;
    }

    @Override
    public SplitQueryType visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        if (ctx.function_with_argtypes() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TRIGGER)) {
            return SplitQueryType.ALTER_TRIGGER;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitIndexstmt(IndexstmtContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitViewstmt(ViewstmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreatecaststmt(CreatecaststmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateopclassstmt(CreateopclassstmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCreateopfamilystmt(CreateopfamilystmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlteropfamilystmt(AlteropfamilystmtContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropcaststmt(DropcaststmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropopclassstmt(DropopclassstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDropopfamilystmt(DropopfamilystmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlteroperatorstmt(AlteroperatorstmtContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDefinestmt(DefinestmtContext ctx) {
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SplitQueryType.CREATE_POLICY;
        } else if (ctx.AGGREGATE() != null || ctx.OPERATOR() != null) {
            return SplitQueryType.CREATE_PROG_OBJ;
        } else if (hasToken(ctx, COLLATION)) {
            return SplitQueryType.CREATE_POLICY;
        } else if (ctx.TYPE_P() != null) {
            return SplitQueryType.CREATE_TYPE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAltercollationstmt(AltercollationstmtContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitAltertsdictionarystmt(AltertsdictionarystmtContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitAltertsconfigurationstmt(AltertsconfigurationstmtContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitSelectstmt(SelectstmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSelect_no_parens(Select_no_parensContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsertstmt(InsertstmtContext ctx) {
        return hasToken(ctx, CONFLICT) ? SplitQueryType.MERGE : SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitMergestmt(MergestmtContext ctx) {
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitUpdatestmt(UpdatestmtContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDeletestmt(DeletestmtContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCreateuserstmt(CreateuserstmtContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitDropuserstmt(DropuserstmtContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitAlterrolestmt(AlterrolestmtContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitAlterrolesetstmt(AlterrolesetstmtContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitCreaterolestmt(CreaterolestmtContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitDroprolestmt(DroprolestmtContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitCreategroupstmt(CreategroupstmtContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitAltergroupstmt(AltergroupstmtContext ctx) {
        return SplitQueryType.ALTER_USER;
    }

    @Override
    public SplitQueryType visitDropgroupstmt(DropgroupstmtContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitRemoveaggrstmt(RemoveaggrstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitRemoveoperstmt(RemoveoperstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitGrantstmt(GrantstmtContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokestmt(RevokestmtContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitGrantrolestmt(GrantrolestmtContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokerolestmt(RevokerolestmtContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitReassignownedstmt(ReassignownedstmtContext ctx) {
        return SplitQueryType.TRANSFER_PRIVILEGE;
    }

    @Override
    public SplitQueryType visitDropownedstmt(DropownedstmtContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitListenstmt(ListenstmtContext ctx) {
        return SplitQueryType.ADMIN_PUB_SUB;
    }

    @Override
    public SplitQueryType visitUnlistenstmt(UnlistenstmtContext ctx) {
        return SplitQueryType.ADMIN_PUB_SUB;
    }

    @Override
    public SplitQueryType visitNotifystmt(NotifystmtContext ctx) {
        return SplitQueryType.ADMIN_PUB_SUB;
    }

    @Override
    public SplitQueryType visitAlterdefaultprivilegesstmt(AlterdefaultprivilegesstmtContext ctx) {
        return hasToken(ctx.defaclaction(), GRANT) ? SplitQueryType.GRANT : SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCallstmt(CallstmtContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitFor_locking_clause(For_locking_clauseContext ctx) {
        return SplitQueryType.QUERY_LOCK;
    }

    @Override
    public SplitQueryType visitLockstmt(LockstmtContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitFetchstmt(FetchstmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCloseportalstmt(CloseportalstmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitDeclarecursorstmt(DeclarecursorstmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitRulestmt(RulestmtContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitCopystmt(CopystmtContext ctx) {
        return ctx.copy_from() != null && ctx.copy_from().FROM() != null ? SplitQueryType.DATA_IMPORT : SplitQueryType.DATA_EXPORT;
    }

    @Override
    public SplitQueryType visitProgram_(Program_Context ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitExplainstmt(ExplainstmtContext ctx) {
        if (isExplainAnalyze(ctx)) {
            return ctx.explainablestmt().accept(this);
        }
        return SplitQueryType.PERFORMANCE;
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
    public SplitQueryType visitTransactionstmt(TransactionstmtContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitLoadstmt(LoadstmtContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitCreateextensionstmt(CreateextensionstmtContext ctx) {
        return SplitQueryType.CREATE_LIBRARY;
    }

    @Override
    public SplitQueryType visitAlterextensionstmt(AlterextensionstmtContext ctx) {
        return SplitQueryType.ALTER_LIBRARY;
    }

    @Override
    public SplitQueryType visitAlterextensioncontentsstmt(AlterextensioncontentsstmtContext ctx) {
        return SplitQueryType.ALTER_LIBRARY;
    }

    @Override
    public SplitQueryType visitVacuumstmt(VacuumstmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitClusterstmt(ClusterstmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitReindexstmt(ReindexstmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitCheckpointstmt(CheckpointstmtContext ctx) {
        return SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitDiscardstmt(DiscardstmtContext ctx) {
        if (ctx.PLANS() != null) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        } else if (ctx.TEMP() != null || ctx.TEMPORARY() != null) {
            return SplitQueryType.DROP_TABLE;
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreatetablespacestmt(CreatetablespacestmtContext ctx) {
        return SplitQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SplitQueryType visitAltertblspcstmt(AltertblspcstmtContext ctx) {
        return SplitQueryType.ALTER_TABLESPACE;
    }

    @Override
    public SplitQueryType visitDroptablespacestmt(DroptablespacestmtContext ctx) {
        return SplitQueryType.DROP_TABLESPACE;
    }

    @Override
    public SplitQueryType visitAlterseqstmt(AlterseqstmtContext ctx) {
        return SplitQueryType.ALTER_SEQUENCE;
    }

    @Override
    public SplitQueryType visitCreatedomainstmt(CreatedomainstmtContext ctx) {
        return SplitQueryType.CREATE_TYPE;
    }

    @Override
    public SplitQueryType visitAlterdomainstmt(AlterdomainstmtContext ctx) {
        return SplitQueryType.ALTER_TYPE;
    }

    @Override
    public SplitQueryType visitAltercompositetypestmt(AltercompositetypestmtContext ctx) {
        return SplitQueryType.ALTER_TYPE;
    }

    @Override
    public SplitQueryType visitAlterenumstmt(AlterenumstmtContext ctx) {
        return SplitQueryType.ALTER_TYPE;
    }

    @Override
    public SplitQueryType visitAltertypestmt(AltertypestmtContext ctx) {
        return SplitQueryType.ALTER_TYPE;
    }

    @Override
    public SplitQueryType visitCreateforeigntablestmt(CreateforeigntablestmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreatefdwstmt(CreatefdwstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterfdwstmt(AlterfdwstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateforeignserverstmt(CreateforeignserverstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAlterforeignserverstmt(AlterforeignserverstmtContext ctx) {
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitCreateusermappingstmt(CreateusermappingstmtContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitAlterusermappingstmt(AlterusermappingstmtContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitDropusermappingstmt(DropusermappingstmtContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitImportforeignschemastmt(ImportforeignschemastmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreateeventtrigstmt(CreateeventtrigstmtContext ctx) {
        return SplitQueryType.CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitAltereventtrigstmt(AltereventtrigstmtContext ctx) {
        return SplitQueryType.ALTER_TRIGGER;
    }

    @Override
    public SplitQueryType visitPreparestmt(PreparestmtContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitExecutestmt(ExecutestmtContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitDeallocatestmt(DeallocatestmtContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitChildren(RuleNode node) {
        if (this.currentNodeOnly) {
            return null;
        }
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SplitQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }
        return SplitQueryType.UNKNOWN;
    }

    private void collectSystemFunctionTypes(Func_applicationContext ctx) {
        String name = normalizeFunctionName(ctx.func_name());
        if (isMetadataFunction(name)) {
            this.metadataReference = true;
            this.types.add(SplitQueryType.METADATA);
            return;
        }
        int previousSize = this.types.size();
        switch (name) {
            case "set_config" -> this.types.add(SplitQueryType.SESSION_SETTING_WRITE);
            case "pg_advisory_lock", "pg_advisory_lock_shared", "pg_advisory_unlock", "pg_advisory_unlock_all", "pg_advisory_unlock_shared", "pg_advisory_xact_lock",
                    "pg_advisory_xact_lock_shared", "pg_try_advisory_lock", "pg_try_advisory_lock_shared", "pg_try_advisory_xact_lock", "pg_try_advisory_xact_lock_shared" ->
                this.types.add(SplitQueryType.SESSION_LOCK);
            case "pg_current_wal_flush_lsn", "pg_current_wal_insert_lsn", "pg_current_wal_lsn", "pg_last_wal_receive_lsn", "pg_last_wal_replay_lsn", "pg_walfile_name",
                    "pg_walfile_name_offset", "pg_wal_lsn_diff", "pg_get_wal_replay_pause_state", "pg_is_wal_replay_paused", "pg_last_xact_replay_timestamp" ->
                this.types.add(SplitQueryType.LOG_READ);
            case "pg_create_restore_point", "pg_switch_wal", "pg_rotate_logfile" -> this.types.add(SplitQueryType.MAINTAIN_LOG);
            case "pg_read_file", "pg_read_binary_file" -> {
                this.types.add(SplitQueryType.DATA_IMPORT);
                this.types.add(SplitQueryType.UNSAFE);
            }
            case "pg_ls_dir", "pg_stat_file" -> {
                this.types.add(SplitQueryType.DATA_IMPORT);
                this.types.add(SplitQueryType.UNSAFE);
            }
            case "pg_ls_tmpdir" -> {
                this.types.add(SplitQueryType.DATA_IMPORT);
                this.types.add(SplitQueryType.UNSAFE);
            }
            case "pg_ls_logdir", "pg_ls_waldir", "pg_ls_archive_statusdir", "pg_current_logfile", "pg_control_checkpoint", "pg_control_init", "pg_control_recovery",
                    "pg_control_system" ->
                this.types.add(SplitQueryType.LOG_READ);
            case "pg_import_system_collations", "pg_reload_conf" -> this.types.add(SplitQueryType.SYSTEM_SETTING_WRITE);
            case "pg_promote" -> this.types.add(SplitQueryType.ALTER_REPLICATION);
            case "pg_wal_replay_pause", "pg_wal_replay_resume", "pg_sync_replication_slots", "pg_log_standby_snapshot", "pg_replication_slot_advance", "pg_logical_emit_message" ->
                this.types.add(SplitQueryType.ADMIN_REPLICATION);
            case "pg_create_physical_replication_slot", "pg_create_logical_replication_slot", "pg_copy_physical_replication_slot", "pg_copy_logical_replication_slot",
                    "pg_replication_origin_create" ->
                this.types.add(SplitQueryType.CREATE_REPLICATION);
            case "pg_drop_replication_slot", "pg_replication_origin_drop" -> this.types.add(SplitQueryType.DROP_REPLICATION);
            case "pg_replication_origin_advance", "pg_replication_origin_session_setup", "pg_replication_origin_session_reset", "pg_replication_origin_xact_setup",
                    "pg_replication_origin_xact_reset" ->
                this.types.add(SplitQueryType.ALTER_REPLICATION);
            case "pg_replication_origin_oid", "pg_replication_origin_progress", "pg_replication_origin_session_is_setup", "pg_replication_origin_session_progress" ->
                this.types.add(SplitQueryType.METADATA);
            case "pg_logical_slot_peek_changes", "pg_logical_slot_peek_binary_changes" -> this.types.add(SplitQueryType.LOG_READ);
            case "pg_logical_slot_get_changes", "pg_logical_slot_get_binary_changes" -> {
                this.types.add(SplitQueryType.LOG_READ);
                this.types.add(SplitQueryType.ADMIN_REPLICATION);
            }
            case "pg_cancel_backend", "pg_terminate_backend", "pg_backup_start", "pg_backup_stop" -> this.types.add(SplitQueryType.ADMIN);
            case "pg_log_backend_memory_contexts" -> this.types.add(SplitQueryType.ADMIN_LOG);
            case "brin_desummarize_range", "brin_summarize_new_values", "brin_summarize_range", "gin_clean_pending_list" -> this.types.add(SplitQueryType.ADMIN_PERFORMANCE);
            case "pg_blocking_pids", "pg_safe_snapshot_blocking_pids", "pg_notification_queue_usage", "pg_mcv_list_items" -> this.types.add(SplitQueryType.PERFORMANCE);
            case "pg_restore_relation_stats", "pg_clear_relation_stats", "pg_restore_attribute_stats", "pg_clear_attribute_stats" -> {
                if (PostgresVersion.ge(this.version, PostgresVersion.POSTGRES_18)) {
                    this.types.add(SplitQueryType.ADMIN_PERFORMANCE);
                }
            }
            case "pg_available_wal_summaries", "pg_wal_summary_contents" -> {
                if (PostgresVersion.ge(this.version, PostgresVersion.POSTGRES_17)) {
                    this.types.add(SplitQueryType.LOG_READ);
                }
            }
            case "pg_ls_summariesdir" -> {
                if (PostgresVersion.ge(this.version, PostgresVersion.POSTGRES_18)) {
                    this.types.add(SplitQueryType.LOG_READ);
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
            return PostgresVersion.ge(this.version, PostgresVersion.POSTGRES_15);
        }
        if (name.equals("pg_column_toast_chunk_id")) {
            return PostgresVersion.ge(this.version, PostgresVersion.POSTGRES_17);
        }
        return name.equals("pg_settings_get_flags") && PostgresVersion.ge(this.version, PostgresVersion.POSTGRES_18);
    }

    private void collectRelationType(Relation_exprContext relation) {
        Qualified_nameContext name = relation.qualified_name();
        String unqualifiedName = normalizeQualifiedName(name);
        if (PERFORMANCE_RELATIONS.contains(unqualifiedName)) {
            this.requiresSelectCarrier = true;
            this.types.add(SplitQueryType.PERFORMANCE);
        } else if (isSystemSchema(name)) {
            this.metadataReference = true;
            this.types.add(SplitQueryType.METADATA);
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

    private boolean isSystemSchema(Qualified_nameContext name) {
        String schema = normalizeName(name.colid().getText());
        return name.indirection() != null && (schema.equals("pg_catalog") || schema.equals("information_schema"));
    }

    private String normalizeName(String name) {
        return name.replace("\"", "").toLowerCase(Locale.ROOT);
    }

}
