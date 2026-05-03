package com.clougence.clouddm.dsfamily.db2.definition.ui.template;

import static com.clougence.utils.NumberUtils.between;

import java.util.Collections;
import java.util.List;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.adapter.db2.Db2Types;
import com.clougence.clouddm.dsfamily.db2.dialect.Db2Dialect;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.CollectionUtils;

public class Db2CmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(SCHEMA_PLACEHOLDER);
        sb.append(".");
        sb.append(TABLE_PLACEHOLDER);
        if (option.getDefaultLimit() > 0) {
            sb.append(" limit ").append(option.getDefaultLimit());
        }
        sb.append(";");
        return sb.toString();
    }

    @Override
    public String getQuickQueryByTable(CmdTemplateOption option) {
        return getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByView(CmdTemplateOption option) {
        return getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByMaterialized(CmdTemplateOption option) {
        return getQuickQuery(option);
    }

    @Override
    public String getQuickQueryByColumn(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(SCHEMA_PLACEHOLDER);
        sb.append(".");
        sb.append(TABLE_PLACEHOLDER);
        sb.append(" where ");
        sb.append(COLUMN_PLACEHOLDER);
        sb.append(" = ");
        return sb.toString();
    }

    @Override
    public List<String> getCreateSchema(CmdTemplateOption option) {
        return Collections.singletonList("create schema " + fmtName(option, option.getSchema()) + ";");
    }

    @Override
    public List<String> getDropSchema(CmdTemplateOption option) {
        return Collections.singletonList("drop schema " + fmtName(option, option.getSchema()) + " restrict;");
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop table ");
        if (option.isUsingExists()) {
            sb.append("if exists ");
        }
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDeleteTable(CmdTemplateOption option) {
        return Collections.singletonList("delete from " + fmtName(option, option.getSchema()) + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("rename table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" to ");
        sb.append(fmtName(option, option.getTargetNewName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getTruncateTable(CmdTemplateOption option) {
        return Collections.singletonList("truncate table " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + " immediate;");
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        return Collections.singletonList("drop view " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getDropMaterialized(CmdTemplateOption option) {
        return Collections.singletonList("drop table " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getDropTrigger(CmdTemplateOption option) {
        return Collections.singletonList("drop trigger " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getCreateView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName()));
        sb.append("\nas\n");
        sb.append(view.getSql());
        if (isNotEmpty(option, Db2AttributeNames.VIEW_CHECK_OPTION)) {
            sb.append("\n");
            sb.append("with ").append(parseString(option, Db2AttributeNames.VIEW_CHECK_OPTION).toLowerCase()).append(" check option");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create trigger ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getName()));
        sb.append("\n");
        sb.append(trigger.getTime()).append(" ");
        String triggerEvent = trigger.getEventList().get(0);
        sb.append(triggerEvent);
        if ("UPDATE".equalsIgnoreCase(triggerEvent) && CollectionUtils.isNotEmpty(trigger.getColumnList())) {
            sb.append(" of ");
            for (int i = 0; i < trigger.getColumnList().size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(fmtName(option, trigger.getColumnList().get(i)));
            }
        }
        sb.append(" on ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getTriggerTable()));
        sb.append("\n");
        String granularity = parseString(option, Db2AttributeNames.TRIGGER_GRANULARITY);
        if ("row".equalsIgnoreCase(granularity)) {
            if (isNotEmpty(option, Db2AttributeNames.NEW_ALIAS) || isNotEmpty(option, Db2AttributeNames.OLD_ALIAS)) {
                sb.append("referencing ");
                if (isNotEmpty(option, Db2AttributeNames.NEW_ALIAS)) {
                    sb.append("new as ").append(fmtName(option, parseString(option, Db2AttributeNames.NEW_ALIAS))).append(" ");
                }
                if (isNotEmpty(option, Db2AttributeNames.OLD_ALIAS)) {
                    sb.append("old as ").append(fmtName(option, parseString(option, Db2AttributeNames.OLD_ALIAS)));
                }
                sb.append("\n");
            }
        }

        sb.append("for each ").append(parseString(option, Db2AttributeNames.TRIGGER_GRANULARITY));
        sb.append("\n");
        if ("row".equalsIgnoreCase(granularity) && isNotEmpty(option, Db2AttributeNames.CONDITION)) {

            sb.append("when(").append(parseString(option, Db2AttributeNames.CONDITION)).append(")");
            sb.append("\n");

        }

        sb.append(trigger.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    private String buildParamType(Param param) {
        Db2Types sqlTypes = Db2Types.valueOfCode(param.getParamType());
        if (sqlTypes == null) {
            return param.getParamType();
        }
        Long length = param.getLength();
        Integer numericPrecision = param.getNumericPrecision();
        Integer numericScale = param.getNumericScale();
        Integer datetimePrecision = param.getDatetimePrecision();
        //
        switch (sqlTypes) {
            case DECIMAL: {
                if (numericPrecision != null && numericScale != null) {
                    return "decimal(" + between(numericPrecision, 0, 31) + ", " + between(numericScale, 0, 31) + ")";
                } else if (numericPrecision != null) {
                    return "decimal(" + between(numericPrecision, 0, 31) + ")";
                } else {
                    return "decimal";
                }
            }
            case CHARACTER:
                return "character" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 255) + ")");
            case VARCHAR: {
                if (length == null || length == 1) {
                    return "varchar(32672)";
                } else if (length == -1 || length > 8000) {
                    return "varchar(32672)";
                } else {
                    return "varchar(" + between(length, 1, 8000) + ")";
                }
            }
            case GRAPHIC:
                return "graphic" + ((length == null || length == 1) ? "" : "(" + between(length, 1, 127) + ")");
            case VARGRAPHIC:
                if (length == null || length == 1) {
                    return "vargraphic";
                } else if (length == -1 || length > 8000) {
                    return "vargraphic(16336)";
                } else {
                    return "vargraphic(" + between(length, 1, 8000) + ")";
                }
            case CLOB: {
                if (length == null || length == 1) {
                    return "clob";
                } else if (length == -1 || length > 8000) {
                    return "clob(1048576)";
                } else {
                    return "clob(" + between(length, 1, 8000) + ")";
                }
            }
            case DBCLOB: {
                if (length == null || length == 1) {
                    return "dbclob";
                } else if (length == -1 || length > 8000) {
                    return "dbclob(1048576)";
                } else {
                    return "dbclob(" + between(length, 1, 8000) + ")";
                }
            }
            case CHAR_FOR_BIT_DATA: {
                if (length == null || length == 1) {
                    return "character for bit data";
                } else if (length == -1 || length > 8000) {
                    return "character(255) for bit data";
                } else {
                    return "character(" + between(length, 1, 8000) + ") for bit data";
                }
            }
            case VARCHAR_FOR_BIT_DATA: {
                if (length == null || length == 1) {
                    return "varchar for bit data";
                } else if (length == -1 || length > 8000) {
                    return "varchar(32672) for bit data";
                } else {
                    return "varchar(" + between(length, 1, 8000) + ") for bit data";
                }
            }
            case LONG_VARCHAR_FOR_BIT_DATA: {
                return "long varchar for bit data";
            }
            case BLOB: {
                if (length == null || length == 1) {
                    return "blob";
                } else if (length == -1 || length > 8000) {
                    return "blob(1048576)";
                } else {
                    return "blob(" + between(length, 1, 8000) + ")";
                }
            }
            case TIMESTAMP:
                return "timestamp" + ((datetimePrecision == null || datetimePrecision == 1) ? "" : "(" + between(datetimePrecision, 1, 12) + ")");
            default:
                return param.getParamType().toLowerCase();
        }
    }

    @Override
    public List<String> getDropTablePrimaryKey(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" drop primary key");
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTableIndex(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop index ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetExactName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    protected Dialect getDialect() { return Db2Dialect.INSTANCE; }
}
