package com.clougence.schema.editor;

import java.util.List;
import java.util.Map;

import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.domain.*;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.schema.umi.special.rdb.RdbForeignKeyRule;
import com.clougence.schema.umi.struts.UmiAttributeNames;

public interface TableEditor extends EditorSource<ETable> {

    List<Action> diffActions(ETable sourceETable, boolean create);

    String catalog();

    void changeCatalog(String newCatalog);

    String schema();

    void changeSchema(String newSchema);

    void rename(String newName);

    void setComment(String comment);

    PrimaryEditor getPrimaryEditor();

    void buildDrop();

    default PrimaryEditor createPrimaryEditor(List<String> columns) {
        return createPrimaryEditor(null, columns);
    }

    PrimaryEditor createPrimaryEditor(String primaryName, List<String> columns);

    default ColumnEditor addColumn(String columnName, String dbType) {
        return this.addColumn(columnName, dbType, null, null, null, null, false, null, null, false, false, null);
    }

    default ColumnEditor addColumn(String columnName) {
        return this.addColumn(columnName, null, null, null, null, null, false, null, null, false, false, null);
    }

    ColumnEditor addColumn(String columnName, String dbType, Boolean nullable, Long length, //
                           Integer numericPrecision, Integer numericScale, boolean unsigned, Integer datetimePrecision, //
                           String defaultValue, boolean defaultIsFunc, //
                           boolean autoGenerate, String comment);

    ConstraintEditor addConstraint(String constraintName, EConstraintType type, String expression, List<String> columns);

    PartitionEditor addPartition(String name, String type, String expression, String description);

    ColumnEditor getColumn(String columnName);

    IndexEditor getIndexEditor(String indexName);

    IndexEditor addIndexEditor(String indexName, EIndexType indexType, List<String> columnName);

    ForeignKeyEditor getForeignKeyEditor(String fkName);

    ForeignKeyEditor addForeignKeyEditor(String fkName, String referenceSchema, String referenceTable, RdbForeignKeyRule updateRule, RdbForeignKeyRule deleteRule,
                                         Map<String, String> referenceMapping);

    List<Action> buildCreate(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildAddColumn(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildModifyColumn(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildChangeColumn(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildDropColumn(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildRenameColumn(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildRenameTableName(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildTruncateTable(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<Action> buildDropTable(SqlBuilder targetBuilder, MainVersion mainVersion);

    List<String> getColumnNames();

    List<String> getForeignKeys();

    List<String> getIndexes();

    void addAttrToContext(String attrKey, String attrValue);

    interface PrimaryEditor extends EditorSource<EPrimaryKey> {

        void addColumn(String[] columnName);

        void removeColumn(String[] columnName);

        void delete();

        void rename(String newName);
    }

    interface ColumnEditor extends EditorSource<EColumn> {

        void rename(String newName);

        void change(String dbType, Boolean nullable, Long length, //
                    Integer numericPrecision, Integer numericScale, boolean unsigned, Integer datetimePrecision, //
                    String defaultValue, boolean defaultIsFunc, //
                    boolean autoGenerate, String comment);

        void changeType(String dbType);

        void setNullable(Boolean isNullable);

        void setCharLength(Long length);

        void setNumberLength(Integer numericPrecision, Integer numericScale);

        void setUnsigned(boolean unsigned);

        void setDateTimePrecision(Integer datetimePrecision);

        void setDefault(String defaultValue, boolean defaultIsFunc);

        void setComment(String comment);

        void setAutoGenerate(boolean autoGenerate);

        void delete();

        void setColumnTypeWithDisplayWidth(String columnTypeWithDisplayWidth);

        void setArrayDimension(Integer arrayDimension);
    }

    interface IndexEditor extends EditorSource<EIndex> {

        void rename(String newName);

        void addColumn(String[] columnName);

        void removeColumn(String[] columnName);

        void delete();

        void setComment(String newComment);
    }

    interface ForeignKeyEditor extends EditorSource<EForeignKey> {

        void rename(String newName);

        void addColumn(String columnName, String referenceColumn);

        void removeColumn(String[] columnName);

        void configUpdateRule(RdbForeignKeyRule foreignKeyRule);

        void configDeleteRule(RdbForeignKeyRule foreignKeyRule);

        void delete();
    }

    interface ConstraintEditor extends EditorSource<EConstraint> {

        void rename(String newName);
    }

    interface PartitionEditor extends EditorSource<EPartition> {

        PartitionDefinitionEditor addDefinition(String type, String expression);

        PartitionItemEditor addItem(String name, String description, String partitionMethod);
    }

    interface PartitionDefinitionEditor extends EditorSource<EPartitionDefinition> {

        void addTemplate(String name, String description);
    }

    interface PartitionItemEditor extends EditorSource<EPartitionItem> {

        PartitionItemEditor addSubItem(String name, String description);
    }

    List<Action> getActions();

    boolean useDelimited();

    TableBuilder createBuilder();

    interface TableBuilder extends Builder {

        TableBuilder changeSchema(String newSchema);

        TableBuilder rename(String newName);

        TableBuilder setComment(String comment);

        ColumnBuilder buildColumn(String name);

        PrimaryBuilder buildPrimaryKey();

        IndexBuilder buildIndex(String name);

        ForeignKeyBuilder buildForeignKey(String name);

        Builder drop();
    }

    interface ColumnBuilder extends Builder {

        ColumnBuilder rename(String newName);

        /**
         * Calling this method may encounter case issues, as FieldType's codeKey does not match the parameter dbType, such as dbType being varchar and codeKey being Varchar
         * Recommend ColumnBuilder changeType(FieldType dbType)
         * @param dbType
         * @return
         */
        @Deprecated
        ColumnBuilder changeType(String dbType);

        ColumnBuilder changeType(FieldType dbType);

        ColumnBuilder setNullable(Boolean isNullable);

        ColumnBuilder setCharLength(long length);

        ColumnBuilder setPrecision(int numericPrecision);

        ColumnBuilder setScale(int numericScale);

        ColumnBuilder setDateTimePrecision(int datetimePrecision);

        ColumnBuilder setDefault(String defaultValue, boolean defaultValueIsFunc);

        ColumnBuilder setComment(String comment);

        ColumnBuilder setAutoGenerate(boolean autoGenerate);

        Builder delete();

        ColumnBuilder setUnsigned(boolean isUnsigned);

        ColumnBuilder putAttribute(UmiAttributeNames name, String value);
    }

    interface PrimaryBuilder extends Builder {

        PrimaryBuilder addColumn(List<String> columnName);

        PrimaryBuilder removeColumn(List<String> columnName);

        PrimaryBuilder rename(String newName);

        Builder delete();
    }

    interface IndexBuilder extends Builder {

        IndexBuilder rename(String newName);

        IndexBuilder addColumn(List<String> columnName);

        IndexBuilder removeColumn(List<String> columnName);

        IndexBuilder configType(EIndexType indexType);

        Builder delete();
    }

    interface ForeignKeyBuilder extends Builder {

        ForeignKeyBuilder rename(String newName);

        ForeignKeyBuilder addColumn(String columnName, String referenceColumn);

        ForeignKeyBuilder removeColumn(List<String> columnName);

        ForeignKeyBuilder configUpdateRule(RdbForeignKeyRule foreignKeyRule);

        ForeignKeyBuilder configDeleteRule(RdbForeignKeyRule foreignKeyRule);

        Builder delete();
    }

    interface PartitionBuilder extends Builder {

        ForeignKeyBuilder rename(String newName);

        ForeignKeyBuilder addPartition(String partitionName, String referenceColumn);

        ForeignKeyBuilder removePartition(List<String> partitionName);

        Builder delete();
    }

    interface Builder {

        boolean isFinish();

        void finish();
    }
}
