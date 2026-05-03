package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public enum MetaInformationType {

    ETable("ETable"),
    // for rdb
    TableDetail(UmiTypes.Table.getTypeName()),
    TableList(UmiTypes.Table.getTypeName() + "List"),
    ViewDetail(UmiTypes.View.getTypeName()),
    ViewList(UmiTypes.View.getTypeName() + "List"),
    FunctionDetail(UmiTypes.Function.getTypeName()),
    FunctionList(UmiTypes.Function.getTypeName() + "List"),
    ProcedureDetail(UmiTypes.Procedure.getTypeName()),
    ProcedureList(UmiTypes.Procedure.getTypeName() + "List"),
    SequenceDetail(UmiTypes.Sequence.getTypeName()),
    SequenceList(UmiTypes.Sequence.getTypeName() + "List"),
    RoleDetail(UmiTypes.ROLE.getTypeName()),
    RoleList(UmiTypes.ROLE.getTypeName() + "List"),
    UserDetail(UmiTypes.USER.getTypeName()),
    UserList(UmiTypes.USER.getTypeName() + "List"),
    SynonymDetail(UmiTypes.Synonym.getTypeName()),
    SynonymList(UmiTypes.Synonym.getTypeName() + "List"),
    DbLinkDetail(UmiTypes.DBLink.getTypeName()),
    DbLinkList(UmiTypes.DBLink.getTypeName() + "List"),
    JobDetail(UmiTypes.Job.getTypeName()),
    JobList(UmiTypes.Job.getTypeName() + "List"),
    ScheduleJobDetail(UmiTypes.ScheduleJob.getTypeName()),
    ScheduleJobList(UmiTypes.ScheduleJob.getTypeName() + "List"),
    MaterializedDetail(UmiTypes.Materialized.getTypeName()),
    MaterializedList(UmiTypes.Materialized.getTypeName() + "List"),
    SchemaDetail(UmiTypes.Schema.getTypeName()),
    SchemaList(UmiTypes.Schema.getTypeName() + "List"),
    CatalogDetail(UmiTypes.Catalog.getTypeName()),
    CatalogList(UmiTypes.Catalog.getTypeName() + "List"),
    TriggerDetail(UmiTypes.Trigger.getTypeName()),
    TriggerList(UmiTypes.Trigger.getTypeName() + "List"),
    ExternalCatalogDetail(UmiTypes.ExternalCatalog.getTypeName()),
    ExternalCatalogList(UmiTypes.ExternalCatalog.getTypeName() + "List"),
    ExternalSchemaDetail(UmiTypes.ExternalSchema.getTypeName()),
    ExternalSchemaList(UmiTypes.ExternalSchema.getTypeName() + "List"),
    ExternalTableDetail(UmiTypes.ExternalTable.getTypeName()),
    ExternalTableList(UmiTypes.ExternalTable.getTypeName() + "List"),
    // for cache
    KeyDetail(UmiTypes.Key.getTypeName()),
    KeyList(UmiTypes.Key.getTypeName() + "List"),;

    private final String typeName;

    MetaInformationType(String typeName){
        this.typeName = typeName;
    }

    public static MetaInformationType valueOfCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (MetaInformationType rdbUmiTypes : MetaInformationType.values()) {
            if (StringUtils.equalsIgnoreCase(rdbUmiTypes.typeName, code)) {
                return rdbUmiTypes;
            }
        }
        return null;
    }
}
