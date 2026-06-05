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
package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.*;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource.JoinType;
import com.clougence.clouddm.ds.sqlserver.analysis.MsSecDomainOptionKeys;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsColumnDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsSelectDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaTable;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.utils.StringUtils;

public abstract class MsAbstractResolveHelper implements MsSecDomainOptionKeys {

    // white list method names (wlMethods)
    protected static final List<String> SimpleQuery_WlMethods = Arrays.asList("SUM", "COUNT", "MAX", "MIN", "AVG", "ADS_VERSION", "VERSION");
    protected static final List<String> Select_As_WlMethods   = Arrays.asList("SUM", "COUNT", "MAX", "MIN", "AVG", "ADS_VERSION", "VERSION");

    protected String getColumnNameRemoveQualifier(String columnNameStr) {
        boolean columnNameQualifier = columnNameStr.length() >= 2 && (columnNameStr.charAt(0) == '[' || columnNameStr.charAt(0) == '\'');
        if (columnNameQualifier) {
            columnNameStr = columnNameStr.substring(1, columnNameStr.length() - 1);
        }
        return columnNameStr;
    }

    protected void rewriteType(MsColumnDomain columnDomain, SQLDataType dataType) {
        String typeName = dataType.getName();

        // char byte
        boolean isCharByte = StringUtils.startsWithIgnoreCase(typeName, "char") && StringUtils.containsIgnoreCase(typeName, " byte");

        // char
        boolean isCharacterVarying = StringUtils.startsWithIgnoreCase(typeName, "character") && StringUtils.containsIgnoreCase(typeName, " varying");
        boolean isChar = (StringUtils.startsWithIgnoreCase(typeName, "char") && !isCharacterVarying && !isCharByte);

        // nchar
        boolean isNationalCharacter = StringUtils.startsWithIgnoreCase(typeName, "national") && StringUtils.containsIgnoreCase(typeName, " char"); // `national char` or `national character`
        boolean isNChar = isNationalCharacter || StringUtils.startsWithIgnoreCase(typeName, "nchar");

        // varchar
        boolean isVarchar = StringUtils.startsWithIgnoreCase(typeName, "varchar") || isCharacterVarying;

        // nvarchar
        boolean isNationalCharacterVarying = StringUtils.startsWithIgnoreCase(typeName, "national") && StringUtils.containsIgnoreCase(typeName, " character")
                                             && StringUtils.containsIgnoreCase(typeName, " varying");
        boolean isNationalVarchar = (StringUtils.startsWithIgnoreCase(typeName, "national") && StringUtils.containsIgnoreCase(typeName, " varchar")) ||//
                                    (StringUtils.startsWithIgnoreCase(typeName, "national") && StringUtils.containsIgnoreCase(typeName, " character")
                                     && StringUtils.containsIgnoreCase(typeName, " varying"));
        boolean isNVarchar = isNationalCharacterVarying || isNationalVarchar || StringUtils.startsWithIgnoreCase(typeName, "nvarchar");

        // other varchar
        boolean isLongVarchar = StringUtils.startsWithIgnoreCase(typeName, "long") && StringUtils.containsIgnoreCase(typeName, " varchar");

        // rewrite type for char
        if (isChar) {
            dataType.setName("char");
            columnDomain.setTypeDesc(dataType.toString());
        } else if (isNChar) {
            dataType.setName("nchar");
            columnDomain.setTypeDesc(dataType.toString());
        } else if (isVarchar) {
            dataType.setName("varchar");
            columnDomain.setTypeDesc(dataType.toString());
        } else if (isNVarchar) {
            dataType.setName("nvarchar");
            columnDomain.setTypeDesc(dataType.toString());
        } else if (isLongVarchar) {
            dataType.setName("mediumtext");
            columnDomain.setTypeDesc(dataType.toString());
        } else {
            columnDomain.setTypeDesc(dataType.toString());
        }

        // double precision
        boolean isDoublePrecision = StringUtils.startsWithIgnoreCase(typeName, "double") && StringUtils.containsIgnoreCase(typeName, " precision");
        if (isDoublePrecision) {
            dataType.setName("double precision");
            columnDomain.setTypeDesc(dataType.toString());
        }

        // binary
        if (isCharByte) {
            dataType.setName("char byte");
            columnDomain.setTypeDesc(dataType.toString());
        }

        boolean isLongBinary = StringUtils.startsWithIgnoreCase(typeName, "long") && StringUtils.containsIgnoreCase(typeName, " binary");
        boolean isLongVarbinary = StringUtils.startsWithIgnoreCase(typeName, "long") && StringUtils.containsIgnoreCase(typeName, " varbinary");
        if (isLongBinary) {
            dataType.setName("long binary");
            columnDomain.setTypeDesc(dataType.toString());
        } else if (isLongVarbinary) {
            dataType.setName("long varbinary");
            columnDomain.setTypeDesc(dataType.toString());
        }
    }

    protected MsColumnDomain toColumnDomain(MsTableDomain tableDomain, SQLColumnDefinition columnDef) {
        SQLDataType dataType = columnDef.getDataType();

        MsColumnDomain domain = new MsColumnDomain();
        //domain.setSqlType(SecQueryType.ALTER_TABLE_ADD_COLUMN);
        //domain.setDdlKind(SqlDdlKind.CREATE);
        domain.setOptions(new HashMap<>());

        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setTable(tableDomain.getTable());
        domain.setColumn(getString(columnDef.getName()));
        domain.setComment(getString(columnDef.getComment()));

        rewriteType(domain, dataType);
        domain.setTypeName(extractTypeName(domain, dataType));
        domain.setNullable(true);
        domain.setPrimary(false);
        domain.setUnique(false);
        domain.setIndex(false);
        domain.setForeign(false);
        //domain.setAuto(columnDef.isAutoIncrement());

        // column types
        if (dataType instanceof SQLCharacterDataType) {
            //domain.setCharacterSet(((SQLCharacterDataType) dataType).getCharSetName());
            //domain.setCollate(((SQLCharacterDataType) dataType).getCollate());
            List<SQLExpr> arg = dataType.getArguments();
            domain.setLength(arg.isEmpty() ? null : arg.get(0).toString());
        } else if (dataType instanceof SQLDataTypeImpl) {

            if (StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "char") || StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "nchar")
                || StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "varchar") || StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "nvarchar")) {
                List<SQLExpr> arg = dataType.getArguments();
                domain.setLength(arg.isEmpty() ? null : arg.get(0).toString());
            }
            if (StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "int") || StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "bigint")
                || StringUtils.startsWithIgnoreCase(domain.getTypeDesc(), "bigint")) {
                List<SQLExpr> arg = dataType.getArguments();
                domain.setLength(arg.isEmpty() ? null : arg.get(0).toString());
            }

            if (((SQLDataTypeImpl) dataType).isZerofill()) {
                //domain.getOptions().put(MySecDomainOptionKeys.OPT_COLUMN_ZEROFILL, "true");
            }
            if (((SQLDataTypeImpl) dataType).isUnsigned()) {
                //domain.getOptions().put(MySecDomainOptionKeys.OPT_COLUMN_UNSIGNED, "true");
            }
        }

        if (columnDef.getDefaultExpr() != null) {
            domain.setDefaultValue(columnDef.getDefaultExpr().toString());
        }

        List<SQLColumnConstraint> constraints = columnDef.getConstraints();
        for (SQLColumnConstraint constraint : constraints) {
            if (constraint instanceof SQLColumnPrimaryKey) {
                domain.setPrimary(true);
                tableDomain.setHasPrimary(true);
            } else if (constraint instanceof SQLNotNullConstraint) {
                domain.setNullable(false);
            } else if (constraint instanceof SQLColumnUniqueKey) {
                domain.setUnique(true);
                tableDomain.setHasUnique(true);
            } else if (constraint instanceof SQLColumnReference) {
                domain.setForeign(true);
                tableDomain.setHasForeignKey(true);
            }
        }

        return domain;
    }

    protected RdbCallDomain toCallDomain(SQLMethodInvokeExpr invokeExpr) {
        return toCallDomain(invokeExpr.getMethodName());
    }

    protected RdbCallDomain toCallDomain(String methodName) {
        RdbCallDomain callDomain = new RdbCallDomain();
        callDomain.setSqlType(SecQueryType.CALL);
        callDomain.setAuditKind(SecQueryKind.CALL);
        callDomain.setOptions(Collections.emptyMap());
        callDomain.setCatalog(null);
        callDomain.setSchema(null);
        callDomain.setCallName(methodName);
        callDomain.setArgs(new ArrayList<>());
        callDomain.setEmptyArg(true);
        return callDomain;
    }

    protected MsSelectDomain newQueryDomain(RdbQueryMode mode) {
        MsSelectDomain domain = new MsSelectDomain();
        domain.setSqlType(SecQueryType.SELECT);
        domain.setAuditKind(SecQueryKind.QUERY);
        domain.setMode(mode);
        domain.setOptions(new HashMap<>());
        domain.setSelectColumns(new ArrayList<>());
        domain.setSelectVariables(new ArrayList<>());
        domain.setSelectFunc(new ArrayList<>());
        domain.setSelectValue(new ArrayList<>());
        domain.setWhereColumns(new ArrayList<>());
        domain.setJoinType(RdbJoinType.NONE);
        return domain;
    }

    protected void configTargetTableName(MetaCollectTables ctxTabs, RdbConfigNames domain, SQLTableSource target) {
        if (target == null) {
            return;
        }

        String c = extractCatalogName(((SQLExprTableSource) target).getName());
        String s = extractSchemaName(((SQLExprTableSource) target).getName());
        String n = extractName(((SQLExprTableSource) target).getName());
        MetaTable ownerTable = ctxTabs.findTableBy(c, s, n);
        if (ownerTable != null) {
            c = ownerTable.getCatalog();
            s = ownerTable.getSchema();
            n = ownerTable.getTable();

            if (!ownerTable.isReal() && domain instanceof RdbSelectDomain) {
                ((RdbSelectDomain) domain).setVirtual(true);
            }
        }

        domain.configName(c, s, n);
    }

    protected MetaTable findOwnerTable(MetaCollectTables ctxTabs, SQLExpr target) {
        //        if (ctxTabs.isEmpty()) {
        //            return null;
        //        }

        String catalog = null;
        String schema = null;
        String table = null;
        String column = null;
        if (target instanceof SQLIdentifierExpr) {
            column = getString(target);
        } else if (target instanceof SQLPropertyExpr) {
            SQLExpr owner = ((SQLPropertyExpr) target).getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                table = getString(owner);
            } else {
                throw new UnsupportedOperationException("unsupported findOwnerTable of " + target.toString());
            }
            column = getColumnNameRemoveQualifier(((SQLPropertyExpr) target).getName());
        } else if (target instanceof SQLInListExpr) {
            column = getString(((SQLInListExpr) target).getExpr());
        } else {
            throw new UnsupportedOperationException("unsupported findOwnerTable of " + target.toString());
        }

        return ctxTabs.findTableBy(catalog, schema, table);
    }

    protected String extractTypeName(MsColumnDomain domain, SQLDataType dataType) {
        return dataType.getName();
    }

    protected String extractName(SQLName sqlName) {
        if (sqlName == null) {
            return null;
        }
        String simpleName = sqlName.getSimpleName();
        boolean hasQualifier = simpleName.charAt(0) == '[';
        if (hasQualifier) {
            return simpleName.substring(1, simpleName.length() - 1);
        } else {
            return simpleName;
        }
    }

    protected String extractName(String sqlName) {
        if (sqlName == null) {
            return null;
        }
        boolean hasQualifier = sqlName.charAt(0) == '[' || sqlName.charAt(0) == '\'';
        if (hasQualifier) {
            return sqlName.substring(1, sqlName.length() - 1);
        } else {
            return sqlName;
        }
    }

    protected String extractCatalogName(SQLName name) {
        if (name instanceof SQLPropertyExpr) {
            SQLExpr owner = ((SQLPropertyExpr) name).getOwner();
            if (owner instanceof SQLPropertyExpr) {
                return getString(((SQLPropertyExpr) owner).getOwner());
            }
        }
        return null;
    }

    protected String extractSchemaName(SQLName tableName) {
        if (tableName instanceof SQLPropertyExpr) {
            SQLExpr owner = ((SQLPropertyExpr) tableName).getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                return getString(owner);
            } else if (owner instanceof SQLPropertyExpr) {
                String nameStr = ((SQLPropertyExpr) owner).getName();
                boolean columnNameQualifier = nameStr.length() >= 2 && (nameStr.charAt(0) == '[');
                if (columnNameQualifier) {
                    nameStr = nameStr.substring(1, nameStr.length() - 1);
                }
                return nameStr;
            }
        }
        return null;
    }

    protected String getString(SQLExpr sqlExpr) {
        if (sqlExpr == null) {
            return null;
        }
        String columnNameStr = null;
        if (sqlExpr instanceof SQLCharExpr) {
            columnNameStr = ((SQLCharExpr) sqlExpr).getText();
        } else if (sqlExpr instanceof SQLIdentifierExpr) {
            columnNameStr = ((SQLIdentifierExpr) sqlExpr).getName();
        } else {
            columnNameStr = sqlExpr.toString();
        }

        boolean columnNameQualifier = columnNameStr.length() >= 2 && (columnNameStr.charAt(0) == '[' || columnNameStr.charAt(0) == '\'');
        if (columnNameQualifier) {
            columnNameStr = columnNameStr.substring(1, columnNameStr.length() - 1);
        }

        return columnNameStr;
    }

    protected JoinType findJoinType(SQLJoinTableSource join) {
        JoinType defaultJoinType = join.getJoinType();

        if (join.getLeft() instanceof SQLJoinTableSource) {
            return findJoinType(join.getLeft(), defaultJoinType);
        } else {
            return defaultJoinType;
        }
    }

    protected JoinType findJoinType(SQLTableSource join, JoinType defaultType) {
        if (join instanceof SQLJoinTableSource) {
            JoinType joinType = ((SQLJoinTableSource) join).getJoinType();
            return findJoinType(((SQLJoinTableSource) join).getLeft(), joinType);
        } else {
            return defaultType;
        }
    }
}
