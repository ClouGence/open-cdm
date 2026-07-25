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

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.postgres.parser.antlr.PgSqlParserBaseVisitor;

public class PgSplitVisitor extends PgSqlParserBaseVisitor<SecQueryType> {

    private static final Set<String> METADATA_FUNCTIONS    = Set
        .of("acldefault", "aclexplode", "col_description", "format_type", "has_any_column_privilege", "has_column_privilege", "has_database_privilege", "has_foreign_data_wrapper_privilege", "has_function_privilege", "has_language_privilege", "has_largeobject_privilege", "has_parameter_privilege", "has_schema_privilege", "has_sequence_privilege", "has_server_privilege", "has_table_privilege", "has_tablespace_privilege", "has_type_privilege", "makeaclitem", "obj_description", "pg_char_to_encoding", "pg_collation_actual_version", "pg_collation_is_visible", "pg_column_compression", "pg_column_size", "pg_conversion_is_visible", "pg_database_size", "pg_describe_object", "pg_encoding_to_char", "pg_filenode_relation", "pg_function_is_visible", "pg_get_catalog_foreign_keys", "pg_get_constraintdef", "pg_get_expr", "pg_get_function_arguments", "pg_get_function_identity_arguments", "pg_get_function_result", "pg_get_functiondef", "pg_get_indexdef", "pg_get_keywords", "pg_get_object_address", "pg_get_partition_constraintdef", "pg_get_partkeydef", "pg_get_ruledef", "pg_get_serial_sequence", "pg_get_statisticsobjdef", "pg_get_triggerdef", "pg_get_userbyid", "pg_get_viewdef", "pg_has_role", "pg_identify_object", "pg_identify_object_as_address", "pg_index_column_has_property", "pg_index_has_property", "pg_indexam_has_property", "pg_indexes_size", "pg_is_in_recovery", "pg_listening_channels", "pg_opclass_is_visible", "pg_operator_is_visible", "pg_opfamily_is_visible", "pg_partition_ancestors", "pg_partition_root", "pg_partition_tree", "pg_relation_filenode", "pg_relation_filepath", "pg_relation_size", "pg_statistics_obj_is_visible", "pg_table_is_visible", "pg_table_size", "pg_tablespace_databases", "pg_tablespace_location", "pg_tablespace_size", "pg_total_relation_size", "pg_ts_config_is_visible", "pg_ts_dict_is_visible", "pg_ts_parser_is_visible", "pg_ts_template_is_visible", "pg_type_is_visible", "pg_typeof", "row_security_active", "shobj_description", "to_regclass", "to_regcollation", "to_regnamespace", "to_regoper", "to_regoperator", "to_regproc", "to_regprocedure", "to_regrole", "to_regtype");

    private static final Set<String> PERFORMANCE_RELATIONS = Set.of("pg_statistic", "pg_statistic_ext", "pg_statistic_ext_data", "pg_stats", "pg_stats_ext", "pg_stats_ext_exprs");

    private final PostgresVersion    version;
    private final Set<SecQueryType>  types                 = new LinkedHashSet<>();
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
    public SecQueryType visit(ParseTree tree) {
        collectTypes(tree);
        return this.types.stream().findFirst().orElse(null);
    }

    public Set<SecQueryType> collectTypes(ParseTree tree) {
        this.types.clear();
        this.metadataReference = false;
        this.ordinaryRelation = false;
        this.requiresSelectCarrier = false;
        collectNode(tree);
        if (this.metadataReference && !this.ordinaryRelation && !this.requiresSelectCarrier) {
            this.types.remove(SecQueryType.SELECT);
        }
        return new LinkedHashSet<>(this.types);
    }

    private void collectNode(ParseTree tree) {
        boolean previous = this.currentNodeOnly;
        SecQueryType type;
        if (isOwnershipTransfer(tree)) {
            type = SecQueryType.TRANSFER_PRIVILEGE;
        } else {
            try {
                this.currentNodeOnly = true;
                type = tree.accept(this);
            } finally {
                this.currentNodeOnly = previous;
            }
        }
        if (tree instanceof ExecutestmtContext && hasToken(tree, CREATE)) {
            this.types.add(SecQueryType.CREATE_TABLE);
        }
        if (type != null) {
            this.types.add(type);
        }
        if (tree instanceof DeclarecursorstmtContext || tree instanceof FetchstmtContext || tree instanceof CloseportalstmtContext) {
            this.types.add(SecQueryType.PROGRAM_CONTROL);
        }
        if (tree instanceof VariablesetstmtContext ctx && ctx.set_rest().SESSION() != null) {
            this.types.add(SecQueryType.SESSION_SETTING_WRITE);
        }
        if (tree instanceof Func_applicationContext function) {
            collectSystemFunctionTypes(function);
        }
        if (tree instanceof Relation_exprContext relation) {
            collectRelationType(relation);
        }
        if (tree instanceof Func_expr_common_subexprContext expression && hasToken(expression, COLLATION) && hasToken(expression, FOR)) {
            this.metadataReference = true;
            this.types.add(SecQueryType.METADATA);
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

    private boolean shouldDescend(ParseTree tree, SecQueryType type) {
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
    public SecQueryType visitDostmt(DostmtContext ctx) {
        return SecQueryType.BLOCK;
    }

    @Override
    public SecQueryType visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitAnalyzestmt(AnalyzestmtContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        return SecQueryType.CREATE_POLICY;
    }

    @Override
    public SecQueryType visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        return SecQueryType.ALTER_POLICY;
    }

    @Override
    public SecQueryType visitCreateseqstmt(CreateseqstmtContext ctx) {
        return SecQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SecQueryType visitTruncatestmt(TruncatestmtContext ctx) {
        return SecQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SecQueryType visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitRename_table_stmt(Rename_table_stmtContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitRename_database_stmt(Rename_database_stmtContext ctx) {
        return SecQueryType.RENAME_CATALOG;
    }

    @Override
    public SecQueryType visitRename_column_stmt(Rename_column_stmtContext ctx) {
        return SecQueryType.RENAME_COLUMN;
    }

    @Override
    public SecQueryType visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        return SecQueryType.RENAME_SCHEMA;
    }

    @Override
    public SecQueryType visitComment_table_stmt(Comment_table_stmtContext ctx) {
        return SecQueryType.COMMENT_TABLE;
    }

    @Override
    public SecQueryType visitComment_column_stmt(Comment_column_stmtContext ctx) {
        return SecQueryType.COMMENT_COLUMN;
    }

    @Override
    public SecQueryType visitCommentstmt(CommentstmtContext ctx) {
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SecQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, ACCESS) && hasToken(ctx, METHOD)) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, RULE) || hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SecQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, TRIGGER)) {
            return SecQueryType.COMMENT_TRIGGER;
        } else if (ctx.aggregate_with_argtypes() != null || ctx.operator_with_argtypes() != null || hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return SecQueryType.COMMENT_PROG_OBJ;
        } else if (hasToken(ctx, ROLE)) {
            return SecQueryType.COMMENT_ROLE;
        } else if (hasToken(ctx, TABLESPACE)) {
            return SecQueryType.COMMENT_TABLESPACE;
        } else if (hasToken(ctx, INDEX)) {
            return SecQueryType.COMMENT_INDEX;
        } else if (hasToken(ctx, DATABASE)) {
            return SecQueryType.COMMENT_CATALOG;
        } else if (hasToken(ctx, SCHEMA)) {
            return SecQueryType.COMMENT_SCHEMA;
        } else if (hasToken(ctx, CONSTRAINT)) {
            return SecQueryType.COMMENT_CONSTRAINT;
        } else if (hasToken(ctx, VIEW)) {
            return SecQueryType.COMMENT_VIEW;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SecQueryType.COMMENT_SEQUENCE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SecQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, PUBLICATION) || hasToken(ctx, SUBSCRIPTION)) {
            return SecQueryType.ALTER_PUB_SUB;
        }
        return visitChildren(ctx);
    }

    @Override
    public SecQueryType visitCreatedbstmt(CreatedbstmtContext ctx) {
        return SecQueryType.CREATE_CATALOG;
    }

    @Override
    public SecQueryType visitDropdbstmt(DropdbstmtContext ctx) {
        return SecQueryType.DROP_CATALOG;
    }

    @Override
    public SecQueryType visitCreateschemastmt(CreateschemastmtContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitVariableshowstmt(VariableshowstmtContext ctx) {
        return SecQueryType.METADATA;
    }

    @Override
    public SecQueryType visitVariablesetstmt(VariablesetstmtContext ctx) {
        Set_restContext set = ctx.set_rest();
        if (set.TRANSACTION() != null || set.SESSION() != null || set.set_rest_more() != null && set.set_rest_more().TRANSACTION() != null) {
            return SecQueryType.TRANSACTION;
        }
        Set_rest_moreContext more = set.set_rest_more();
        if (more != null && more.ROLE() != null) {
            return SecQueryType.SWITCH_ROLE;
        }
        if (more != null && more.SESSION() != null && more.AUTHORIZATION() != null) {
            return SecQueryType.SWITCH_USER;
        }
        if (more != null && more.CATALOG() != null) {
            return SecQueryType.SWITCH_CATALOG;
        }
        if (more != null && more.SCHEMA() != null) {
            return SecQueryType.SWITCH_SCHEMA;
        }
        return SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitVariableresetstmt(VariableresetstmtContext ctx) {
        Reset_restContext reset = ctx.reset_rest();
        if (reset.TRANSACTION() != null) {
            return SecQueryType.TRANSACTION;
        }
        if (reset.SESSION() != null && reset.AUTHORIZATION() != null) {
            return SecQueryType.SWITCH_USER;
        }
        return SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitConstraintssetstmt(ConstraintssetstmtContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitAltersystemstmt(AltersystemstmtContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateamstmt(CreateamstmtContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateconversionstmt(CreateconversionstmtContext ctx) {
        return SecQueryType.CREATE_POLICY;
    }

    @Override
    public SecQueryType visitCreatestatsstmt(CreatestatsstmtContext ctx) {
        return SecQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SecQueryType visitAlterstatsstmt(AlterstatsstmtContext ctx) {
        return SecQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SecQueryType visitDropschemastmt(DropschemastmtContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterownerstmt(AlterownerstmtContext ctx) {
        return SecQueryType.TRANSFER_PRIVILEGE;
    }

    @Override
    public SecQueryType visitAlterobjectschemastmt(AlterobjectschemastmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return SecQueryType.ALTER_PROG_OBJ;
        }
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SecQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SecQueryType.ALTER_SEQUENCE;
        } else if (hasToken(ctx, VIEW)) {
            return SecQueryType.ALTER_VIEW;
        } else if (hasToken(ctx, TABLE) || hasToken(ctx, FOREIGN)) {
            return SecQueryType.ALTER_TABLE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SecQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, EXTENSION)) {
            return SecQueryType.ALTER_LIBRARY;
        } else if (hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SecQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return SecQueryType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return SecQueryType.ALTER_TYPE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitCreatestmt(CreatestmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitCreateasstmt(CreateasstmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitAltertablestmt(AltertablestmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == TABLE) {
                return SecQueryType.ALTER_TABLE;

            } else if (childNode.getSymbol().getType() == INDEX) {
                return SecQueryType.ALTER_INDEX;

            } else if (childNode.getSymbol().getType() == VIEW) {
                return SecQueryType.ALTER_VIEW;

            }
        }
        if (hasToken(ctx, MATERIALIZED) && hasToken(ctx, VIEW)) {
            return SecQueryType.ALTER_VIEW;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return SecQueryType.ALTER_TABLE;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SecQueryType.ALTER_SEQUENCE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAddColumn(AddColumnContext ctx) {
        return SecQueryType.ADD_COLUMN;
    }

    @Override
    public SecQueryType visitAlterColumn(AlterColumnContext ctx) {
        return SecQueryType.ALTER_COLUMN;
    }

    @Override
    public SecQueryType visitDropColumn(DropColumnContext ctx) {
        return SecQueryType.DROP_COLUMN;
    }

    @Override
    public SecQueryType visitAddConstraint(AddConstraintContext ctx) {
        return SecQueryType.ADD_CONSTRAINT;
    }

    @Override
    public SecQueryType visitAlterConstaint(AlterConstaintContext ctx) {
        return SecQueryType.ALTER_CONSTRAINT;
    }

    @Override
    public SecQueryType visitValidateConstraint(ValidateConstraintContext ctx) {
        return SecQueryType.ALTER_CONSTRAINT;
    }

    @Override
    public SecQueryType visitDropConstraint(DropConstraintContext ctx) {
        return SecQueryType.DROP_CONSTRAINT;
    }

    @Override
    public SecQueryType visitPartition_cmd(Partition_cmdContext ctx) {
        return SecQueryType.ALTER_PARTITION;
    }

    @Override
    public SecQueryType visitDroptablestmt(DroptablestmtContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitDropstmt(DropstmtContext ctx) {
        if (hasToken(ctx, EXTENSION)) {
            return SecQueryType.DROP_LIBRARY;
        } else if (hasToken(ctx, INDEX)) {
            return SecQueryType.DROP_INDEX;
        } else if (hasToken(ctx, VIEW)) {
            return SecQueryType.DROP_VIEW;
        } else if (hasToken(ctx, TRIGGER) && !hasToken(ctx, EVENT)) {
            return SecQueryType.DROP_TRIGGER;
        } else if (hasToken(ctx, POLICY)) {
            return SecQueryType.DROP_POLICY;
        } else if (hasToken(ctx, RULE)) {
            return SecQueryType.DROP_POLICY;
        } else if (hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SecQueryType.DROP_POLICY;
        } else if (hasToken(ctx, PUBLICATION)) {
            return SecQueryType.DROP_PUB_SUB;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SecQueryType.DROP_SEQUENCE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return SecQueryType.DROP_TYPE;
        } else if (hasToken(ctx, EVENT) && hasToken(ctx, TRIGGER)) {
            return SecQueryType.DROP_TRIGGER;
        } else if (hasToken(ctx, TABLESPACE)) {
            return SecQueryType.DROP_TABLESPACE;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, TABLE)) {
            return SecQueryType.DROP_TABLE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SecQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, ACCESS) && hasToken(ctx, METHOD)) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SecQueryType.DROP_POLICY;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, WRAPPER) || hasToken(ctx, SERVER)) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        }
        return SecQueryType.UNKNOWN;
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
    public SecQueryType visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitRenamestmt(RenamestmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null) {
            return SecQueryType.RENAME_PROG_OBJ;
        } else if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SecQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, FOREIGN) && hasToken(ctx, WRAPPER) || hasToken(ctx, SERVER)) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        } else if (hasToken(ctx, RULE) || hasToken(ctx, COLLATION) || hasToken(ctx, CONVERSION_P)) {
            return SecQueryType.ALTER_POLICY;
        } else if (hasToken(ctx, OPERATOR) && (hasToken(ctx, CLASS) || hasToken(ctx, FAMILY))) {
            return SecQueryType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TRIGGER)) {
            return SecQueryType.RENAME_TRIGGER;
        } else if (hasToken(ctx, USER)) {
            return SecQueryType.RENAME_USER;
        } else if (hasToken(ctx, ROLE) || hasToken(ctx, GROUP_P)) {
            return SecQueryType.RENAME_ROLE;
        } else if (hasToken(ctx, VIEW) && hasToken(ctx, COLUMN)) {
            return SecQueryType.RENAME_COLUMN;
        } else if (hasToken(ctx, VIEW)) {
            return SecQueryType.RENAME_VIEW;
        } else if (hasToken(ctx, PUBLICATION)) {
            return SecQueryType.ALTER_PUB_SUB;
        } else if (hasToken(ctx, SUBSCRIPTION)) {
            return SecQueryType.ALTER_PUB_SUB;
        } else if (hasToken(ctx, SEQUENCE)) {
            return SecQueryType.RENAME_SEQUENCE;
        } else if (hasToken(ctx, TABLESPACE)) {
            return SecQueryType.RENAME_TABLESPACE;
        } else if (hasToken(ctx, STATISTICS)) {
            return SecQueryType.ADMIN_PERFORMANCE;
        } else if (hasToken(ctx, DOMAIN_P) && hasToken(ctx, CONSTRAINT)) {
            return SecQueryType.RENAME_CONSTRAINT;
        } else if (hasToken(ctx, TYPE_P) && hasToken(ctx, ATTRIBUTE)) {
            return SecQueryType.ALTER_TYPE;
        } else if (hasToken(ctx, TYPE_P) || hasToken(ctx, DOMAIN_P)) {
            return SecQueryType.RENAME_TYPE;
        }
        return visitChildren(ctx);
    }

    @Override
    public SecQueryType visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        return SecQueryType.CREATE_PUB_SUB;
    }

    @Override
    public SecQueryType visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        return SecQueryType.ALTER_PUB_SUB;
    }

    @Override
    public SecQueryType visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        return SecQueryType.CREATE_PUB_SUB;
    }

    @Override
    public SecQueryType visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        if (ctx.ENABLE_P() != null || ctx.DISABLE_P() != null || ctx.REFRESH() != null || ctx.SKIP_P() != null) {
            return SecQueryType.ADMIN_PUB_SUB;
        }
        return SecQueryType.ALTER_PUB_SUB;
    }

    @Override
    public SecQueryType visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        return SecQueryType.DROP_PUB_SUB;
    }

    @Override
    public SecQueryType visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        if (ctx.function_with_argtypes() != null) {
            return SecQueryType.ALTER_PROG_OBJ;
        } else if (hasToken(ctx, TRIGGER)) {
            return SecQueryType.ALTER_TRIGGER;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitIndexstmt(IndexstmtContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitViewstmt(ViewstmtContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitCreatecaststmt(CreatecaststmtContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitCreateopclassstmt(CreateopclassstmtContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitCreateopfamilystmt(CreateopfamilystmtContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitAlteropfamilystmt(AlteropfamilystmtContext ctx) {
        return SecQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SecQueryType visitDropcaststmt(DropcaststmtContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitDropopclassstmt(DropopclassstmtContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitDropopfamilystmt(DropopfamilystmtContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        return SecQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SecQueryType visitAlteroperatorstmt(AlteroperatorstmtContext ctx) {
        return SecQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SecQueryType visitDefinestmt(DefinestmtContext ctx) {
        if (hasToken(ctx, TEXT_P) && hasToken(ctx, SEARCH)) {
            return SecQueryType.CREATE_POLICY;
        } else if (ctx.AGGREGATE() != null || ctx.OPERATOR() != null) {
            return SecQueryType.CREATE_PROG_OBJ;
        } else if (hasToken(ctx, COLLATION)) {
            return SecQueryType.CREATE_POLICY;
        } else if (ctx.TYPE_P() != null) {
            return SecQueryType.CREATE_TYPE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAltercollationstmt(AltercollationstmtContext ctx) {
        return SecQueryType.ALTER_POLICY;
    }

    @Override
    public SecQueryType visitAltertsdictionarystmt(AltertsdictionarystmtContext ctx) {
        return SecQueryType.ALTER_POLICY;
    }

    @Override
    public SecQueryType visitAltertsconfigurationstmt(AltertsconfigurationstmtContext ctx) {
        return SecQueryType.ALTER_POLICY;
    }

    @Override
    public SecQueryType visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitSelectstmt(SelectstmtContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitSelect_no_parens(Select_no_parensContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsertstmt(InsertstmtContext ctx) {
        return hasToken(ctx, CONFLICT) ? SecQueryType.MERGE : SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitMergestmt(MergestmtContext ctx) {
        return SecQueryType.MERGE;
    }

    @Override
    public SecQueryType visitUpdatestmt(UpdatestmtContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDeletestmt(DeletestmtContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCreateuserstmt(CreateuserstmtContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitDropuserstmt(DropuserstmtContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitAlterrolestmt(AlterrolestmtContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitAlterrolesetstmt(AlterrolesetstmtContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitCreaterolestmt(CreaterolestmtContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitDroprolestmt(DroprolestmtContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitCreategroupstmt(CreategroupstmtContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitAltergroupstmt(AltergroupstmtContext ctx) {
        return SecQueryType.ALTER_USER;
    }

    @Override
    public SecQueryType visitDropgroupstmt(DropgroupstmtContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitRemoveaggrstmt(RemoveaggrstmtContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitRemoveoperstmt(RemoveoperstmtContext ctx) {
        return SecQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SecQueryType visitGrantstmt(GrantstmtContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokestmt(RevokestmtContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitGrantrolestmt(GrantrolestmtContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokerolestmt(RevokerolestmtContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitReassignownedstmt(ReassignownedstmtContext ctx) {
        return SecQueryType.TRANSFER_PRIVILEGE;
    }

    @Override
    public SecQueryType visitDropownedstmt(DropownedstmtContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitListenstmt(ListenstmtContext ctx) {
        return SecQueryType.ADMIN_PUB_SUB;
    }

    @Override
    public SecQueryType visitUnlistenstmt(UnlistenstmtContext ctx) {
        return SecQueryType.ADMIN_PUB_SUB;
    }

    @Override
    public SecQueryType visitNotifystmt(NotifystmtContext ctx) {
        return SecQueryType.ADMIN_PUB_SUB;
    }

    @Override
    public SecQueryType visitAlterdefaultprivilegesstmt(AlterdefaultprivilegesstmtContext ctx) {
        return hasToken(ctx.defaclaction(), GRANT) ? SecQueryType.GRANT : SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCallstmt(CallstmtContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitFor_locking_clause(For_locking_clauseContext ctx) {
        return SecQueryType.QUERY_LOCK;
    }

    @Override
    public SecQueryType visitLockstmt(LockstmtContext ctx) {
        return SecQueryType.SESSION_LOCK;
    }

    @Override
    public SecQueryType visitFetchstmt(FetchstmtContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitCloseportalstmt(CloseportalstmtContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitDeclarecursorstmt(DeclarecursorstmtContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitRulestmt(RulestmtContext ctx) {
        return SecQueryType.CREATE_POLICY;
    }

    @Override
    public SecQueryType visitCopystmt(CopystmtContext ctx) {
        return ctx.copy_from() != null && ctx.copy_from().FROM() != null ? SecQueryType.DATA_IMPORT : SecQueryType.DATA_EXPORT;
    }

    @Override
    public SecQueryType visitProgram_(Program_Context ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitExplainstmt(ExplainstmtContext ctx) {
        if (isExplainAnalyze(ctx)) {
            return ctx.explainablestmt().accept(this);
        }
        return SecQueryType.PERFORMANCE;
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
    public SecQueryType visitTransactionstmt(TransactionstmtContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitLoadstmt(LoadstmtContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitCreateextensionstmt(CreateextensionstmtContext ctx) {
        return SecQueryType.CREATE_LIBRARY;
    }

    @Override
    public SecQueryType visitAlterextensionstmt(AlterextensionstmtContext ctx) {
        return SecQueryType.ALTER_LIBRARY;
    }

    @Override
    public SecQueryType visitAlterextensioncontentsstmt(AlterextensioncontentsstmtContext ctx) {
        return SecQueryType.ALTER_LIBRARY;
    }

    @Override
    public SecQueryType visitVacuumstmt(VacuumstmtContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitClusterstmt(ClusterstmtContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitReindexstmt(ReindexstmtContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitCheckpointstmt(CheckpointstmtContext ctx) {
        return SecQueryType.MAINTAIN_LOG;
    }

    @Override
    public SecQueryType visitDiscardstmt(DiscardstmtContext ctx) {
        if (ctx.PLANS() != null) {
            return SecQueryType.ADMIN_PERFORMANCE;
        } else if (ctx.TEMP() != null || ctx.TEMPORARY() != null) {
            return SecQueryType.DROP_TABLE;
        }
        return SecQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreatetablespacestmt(CreatetablespacestmtContext ctx) {
        return SecQueryType.CREATE_TABLESPACE;
    }

    @Override
    public SecQueryType visitAltertblspcstmt(AltertblspcstmtContext ctx) {
        return SecQueryType.ALTER_TABLESPACE;
    }

    @Override
    public SecQueryType visitDroptablespacestmt(DroptablespacestmtContext ctx) {
        return SecQueryType.DROP_TABLESPACE;
    }

    @Override
    public SecQueryType visitAlterseqstmt(AlterseqstmtContext ctx) {
        return SecQueryType.ALTER_SEQUENCE;
    }

    @Override
    public SecQueryType visitCreatedomainstmt(CreatedomainstmtContext ctx) {
        return SecQueryType.CREATE_TYPE;
    }

    @Override
    public SecQueryType visitAlterdomainstmt(AlterdomainstmtContext ctx) {
        return SecQueryType.ALTER_TYPE;
    }

    @Override
    public SecQueryType visitAltercompositetypestmt(AltercompositetypestmtContext ctx) {
        return SecQueryType.ALTER_TYPE;
    }

    @Override
    public SecQueryType visitAlterenumstmt(AlterenumstmtContext ctx) {
        return SecQueryType.ALTER_TYPE;
    }

    @Override
    public SecQueryType visitAltertypestmt(AltertypestmtContext ctx) {
        return SecQueryType.ALTER_TYPE;
    }

    @Override
    public SecQueryType visitCreateforeigntablestmt(CreateforeigntablestmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitCreatefdwstmt(CreatefdwstmtContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitAlterfdwstmt(AlterfdwstmtContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateforeignserverstmt(CreateforeignserverstmtContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitAlterforeignserverstmt(AlterforeignserverstmtContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitCreateusermappingstmt(CreateusermappingstmtContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitAlterusermappingstmt(AlterusermappingstmtContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitDropusermappingstmt(DropusermappingstmtContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitImportforeignschemastmt(ImportforeignschemastmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitCreateeventtrigstmt(CreateeventtrigstmtContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitAltereventtrigstmt(AltereventtrigstmtContext ctx) {
        return SecQueryType.ALTER_TRIGGER;
    }

    @Override
    public SecQueryType visitPreparestmt(PreparestmtContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitExecutestmt(ExecutestmtContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitDeallocatestmt(DeallocatestmtContext ctx) {
        return SecQueryType.UNSAFE;
    }

    @Override
    public SecQueryType visitChildren(RuleNode node) {
        if (this.currentNodeOnly) {
            return null;
        }
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SecQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }
        return SecQueryType.UNKNOWN;
    }

    private void collectSystemFunctionTypes(Func_applicationContext ctx) {
        String name = normalizeFunctionName(ctx.func_name());
        if (isMetadataFunction(name)) {
            this.metadataReference = true;
            this.types.add(SecQueryType.METADATA);
            return;
        }
        int previousSize = this.types.size();
        switch (name) {
            case "set_config" -> this.types.add(SecQueryType.SESSION_SETTING_WRITE);
            case "pg_advisory_lock", "pg_advisory_lock_shared", "pg_advisory_unlock", "pg_advisory_unlock_all", "pg_advisory_unlock_shared", "pg_advisory_xact_lock",
                    "pg_advisory_xact_lock_shared", "pg_try_advisory_lock", "pg_try_advisory_lock_shared", "pg_try_advisory_xact_lock", "pg_try_advisory_xact_lock_shared" ->
                this.types.add(SecQueryType.SESSION_LOCK);
            case "pg_current_wal_flush_lsn", "pg_current_wal_insert_lsn", "pg_current_wal_lsn", "pg_last_wal_receive_lsn", "pg_last_wal_replay_lsn", "pg_walfile_name",
                    "pg_walfile_name_offset", "pg_wal_lsn_diff", "pg_get_wal_replay_pause_state", "pg_is_wal_replay_paused", "pg_last_xact_replay_timestamp" ->
                this.types.add(SecQueryType.LOG_READ);
            case "pg_create_restore_point", "pg_switch_wal", "pg_rotate_logfile" -> this.types.add(SecQueryType.MAINTAIN_LOG);
            case "pg_read_file", "pg_read_binary_file" -> {
                this.types.add(SecQueryType.DATA_IMPORT);
                this.types.add(SecQueryType.UNSAFE);
            }
            case "pg_ls_dir", "pg_stat_file" -> {
                this.types.add(SecQueryType.DATA_IMPORT);
                this.types.add(SecQueryType.UNSAFE);
            }
            case "pg_ls_tmpdir" -> {
                this.types.add(SecQueryType.DATA_IMPORT);
                this.types.add(SecQueryType.UNSAFE);
            }
            case "pg_ls_logdir", "pg_ls_waldir", "pg_ls_archive_statusdir", "pg_current_logfile", "pg_control_checkpoint", "pg_control_init", "pg_control_recovery",
                    "pg_control_system" ->
                this.types.add(SecQueryType.LOG_READ);
            case "pg_import_system_collations", "pg_reload_conf" -> this.types.add(SecQueryType.SYSTEM_SETTING_WRITE);
            case "pg_promote" -> this.types.add(SecQueryType.ALTER_REPLICATION);
            case "pg_wal_replay_pause", "pg_wal_replay_resume", "pg_sync_replication_slots", "pg_log_standby_snapshot", "pg_replication_slot_advance", "pg_logical_emit_message" ->
                this.types.add(SecQueryType.ADMIN_REPLICATION);
            case "pg_create_physical_replication_slot", "pg_create_logical_replication_slot", "pg_copy_physical_replication_slot", "pg_copy_logical_replication_slot",
                    "pg_replication_origin_create" ->
                this.types.add(SecQueryType.CREATE_REPLICATION);
            case "pg_drop_replication_slot", "pg_replication_origin_drop" -> this.types.add(SecQueryType.DROP_REPLICATION);
            case "pg_replication_origin_advance", "pg_replication_origin_session_setup", "pg_replication_origin_session_reset", "pg_replication_origin_xact_setup",
                    "pg_replication_origin_xact_reset" ->
                this.types.add(SecQueryType.ALTER_REPLICATION);
            case "pg_replication_origin_oid", "pg_replication_origin_progress", "pg_replication_origin_session_is_setup", "pg_replication_origin_session_progress" ->
                this.types.add(SecQueryType.METADATA);
            case "pg_logical_slot_peek_changes", "pg_logical_slot_peek_binary_changes" -> this.types.add(SecQueryType.LOG_READ);
            case "pg_logical_slot_get_changes", "pg_logical_slot_get_binary_changes" -> {
                this.types.add(SecQueryType.LOG_READ);
                this.types.add(SecQueryType.ADMIN_REPLICATION);
            }
            case "pg_cancel_backend", "pg_terminate_backend", "pg_backup_start", "pg_backup_stop" -> this.types.add(SecQueryType.ADMIN);
            case "pg_log_backend_memory_contexts" -> this.types.add(SecQueryType.ADMIN_LOG);
            case "brin_desummarize_range", "brin_summarize_new_values", "brin_summarize_range", "gin_clean_pending_list" -> this.types.add(SecQueryType.ADMIN_PERFORMANCE);
            case "pg_blocking_pids", "pg_safe_snapshot_blocking_pids", "pg_notification_queue_usage", "pg_mcv_list_items" -> this.types.add(SecQueryType.PERFORMANCE);
            case "pg_restore_relation_stats", "pg_clear_relation_stats", "pg_restore_attribute_stats", "pg_clear_attribute_stats" -> {
                if (this.version.atLeast(PostgresVersion.POSTGRES_18)) {
                    this.types.add(SecQueryType.ADMIN_PERFORMANCE);
                }
            }
            case "pg_available_wal_summaries", "pg_wal_summary_contents" -> {
                if (this.version.atLeast(PostgresVersion.POSTGRES_17)) {
                    this.types.add(SecQueryType.LOG_READ);
                }
            }
            case "pg_ls_summariesdir" -> {
                if (this.version.atLeast(PostgresVersion.POSTGRES_18)) {
                    this.types.add(SecQueryType.LOG_READ);
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
            this.types.add(SecQueryType.PERFORMANCE);
        } else if (isSystemSchema(name)) {
            this.metadataReference = true;
            this.types.add(SecQueryType.METADATA);
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
