package com.clougence.clouddm.ds.oracle.definition.ui.template;

import static com.clougence.adapter.oracle.OracleAttributeNames.NEW_ALIAS;
import static com.clougence.adapter.oracle.OracleAttributeNames.OLD_ALIAS;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.dblink.OraDbLinkFields;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.job.OraJobFields;
import com.clougence.clouddm.ds.oracle.definition.ui.editor.schedule.OraScheduleJobFields;
import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
import com.clougence.clouddm.sdk.ui.editor.job.JobEditorFields;
import com.clougence.clouddm.sdk.ui.template.AbstractCmdTemplateSpi;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.schema.dialect.Dialect;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

public class OraCmdTemplateSpi extends AbstractCmdTemplateSpi {

    @Override
    public String getQuickQuery(CmdTemplateOption option) {
        return "select * from " + SCHEMA_PLACEHOLDER + "." + TABLE_PLACEHOLDER + ";";
    }

    public String getQuickQueryByScheduleJob(CmdTemplateOption option) {
        String sql = "begin\n  dbms_scheduler.run_job(job_name => '%s', use_current_session => FALSE);\nend;";
        return String.format(sql, TABLE_PLACEHOLDER);
    }

    public String getQuickQueryByJob(CmdTemplateOption option) {
        String sql = "begin\n    dbms_job.run(%s);\nend;";;
        return String.format(sql, TABLE_PLACEHOLDER);
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
        sb.append(TABLE_PLACEHOLDER);
        sb.append(" where ");
        sb.append(COLUMN_PLACEHOLDER);
        sb.append(" = ");
        return sb.toString();
    }

    @Override
    public String getQuickQueryByProcedure(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("call ");
        sb.append(TABLE_PLACEHOLDER);
        sb.append("(");
        sb.append(PARAM_PLACEHOLDER);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getQuickQueryByFunction(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(TABLE_PLACEHOLDER);
        sb.append("(");
        sb.append(PARAM_PLACEHOLDER);
        sb.append(")");
        sb.append(" from DUAL");
        return sb.toString();
    }

    @Override
    public List<String> getDropTable(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop table ");
        sb.append(fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" cascade");
        }
        if (option.isPurge()) {
            sb.append(" constraints purge");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDeleteTable(CmdTemplateOption option) {
        return Collections.singletonList("delete from " + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getRenameTable(CmdTemplateOption option) {
        return Collections.singletonList("alter table " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + " rename to "
                                         + fmtName(option, option.getTargetNewName()) + ";");
    }

    @Override
    public List<String> getTruncateTable(CmdTemplateOption option) {
        return Collections.singletonList("truncate table " + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getDropView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop view ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(".");
        sb.append(fmtName(option, option.getTargetName()));
        if (option.isCascade()) {
            sb.append(" cascade constraints");
        }
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropTrigger(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("drop trigger ");
        sb.append(fmtName(option, option.getSchema()));
        sb.append(".");
        sb.append(fmtName(option, option.getTargetName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        option.setDelimited(true);
        StringBuilder sb = new StringBuilder();
        sb.append("create ");
        if (option.isReplace()) {
            sb.append("or replace ");
        }
        sb.append("trigger ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, trigger.getName()));
        sb.append(" \n");
        sb.append(trigger.getTime());
        sb.append(" ");
        for (int i = 0; i < trigger.getEventList().size(); i++) {
            if (i != 0) {
                sb.append("or ");
            }
            String event = trigger.getEventList().get(i);
            sb.append(event + " ");
            if (event.equals("update")) {
                List<String> columns = trigger.getColumnList();
                if (columns != null && columns.size() != 0) {
                    sb.append("of ");
                    for (int j = 0; j < columns.size(); j++) {
                        if (j != 0) {
                            sb.append(",");
                        }
                        sb.append(fmtName(option, columns.get(j)));
                    }
                }
                sb.append(" ");
            }
        }
        sb.append("\non ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getTriggerTable()));
        sb.append("\n");
        if (isNotEmpty(option, NEW_ALIAS) || isNotEmpty(option, OLD_ALIAS)) {
            sb.append("referencing");
            if (isNotEmpty(option, NEW_ALIAS)) {
                sb.append(" new as ").append(parseString(option, NEW_ALIAS));
            }
            if (isNotEmpty(option, OLD_ALIAS)) {
                sb.append(" old as ").append(parseString(option, OLD_ALIAS));
            }
            sb.append("\n");
        }
        if (!parseBoolean(option, OracleAttributeNames.TRIGGER_GRANULARITY)) {
            sb.append("for each row \n");
        }
        if (isNotEmpty(option, OracleAttributeNames.CONDITION)) {
            sb.append("when(").append(parseString(option, OracleAttributeNames.CONDITION)).append(")\n");
        }
        sb.append(trigger.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getAlterTrigger(CmdTemplateOption option) {
        Trigger trigger = convertToTrigger(option.getData());
        option.setDelimited(true);
        StringBuilder sb = new StringBuilder();
        sb.append("create ");
        sb.append("or replace ");

        sb.append("trigger ");
        sb.append(fmtName(option, option.getSchema()) + "." + fmtName(option, trigger.getName()));
        sb.append(" \n");
        sb.append(trigger.getTime());
        sb.append(" ");
        for (int i = 0; i < trigger.getEventList().size(); i++) {
            if (i != 0) {
                sb.append("or ");
            }
            String event = trigger.getEventList().get(i);
            sb.append(event + " ");
            if (event.equals("update")) {
                List<String> columns = trigger.getColumnList();
                if (columns != null && columns.size() != 0) {
                    sb.append("of ");
                    for (int j = 0; j < columns.size(); j++) {
                        if (j != 0) {
                            sb.append(",");
                        }
                        sb.append(fmtName(option, columns.get(j)));
                    }
                }
                sb.append(" ");
            }
        }
        sb.append("\non ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, trigger.getTriggerTable()));
        sb.append("\n");
        if (isNotEmpty(option, NEW_ALIAS) || isNotEmpty(option, OLD_ALIAS)) {
            sb.append("referencing");
            if (isNotEmpty(option, NEW_ALIAS)) {
                sb.append(" new as ").append(parseString(option, NEW_ALIAS));
            }
            if (isNotEmpty(option, OLD_ALIAS)) {
                sb.append(" old as ").append(parseString(option, OLD_ALIAS));
            }
            sb.append("\n");
        }
        if (!parseBoolean(option, OracleAttributeNames.TRIGGER_GRANULARITY)) {
            sb.append("for each row \n");
        }
        if (isNotEmpty(option, OracleAttributeNames.CONDITION)) {
            sb.append("when(").append(parseString(option, OracleAttributeNames.CONDITION)).append(")\n");
        }
        sb.append(trigger.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create ");

        if (parseBoolean(option, OracleAttributeNames.FORCE_CREATE_VIEW)) {
            sb.append("force ");
        }
        sb.append("view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName())).append("\n");
        sb.append("as");
        sb.append("\n");
        sb.append(view.getSql());
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropFunction(CmdTemplateOption option) {
        return Collections.singletonList("drop function " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getCreateFunction(CmdTemplateOption option) {
        Function function = convertToFunction(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create function ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, function.getName()));
        // start params
        if (CollectionUtils.isNotEmpty(function.getParams())) {
            sb.append("(");
            for (int i = 0; i < function.getParams().size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                Param param = function.getParams().get(i);
                sb.append(parseParam(param));
            }
            sb.append(")");
        }

        sb.append("\nreturn ").append(function.getReturnType().getParamType());
        sb.append("\n").append("as").append("\n");
        sb.append(function.getSql());
        sb.append(";");

        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getCreateProcedure(CmdTemplateOption option) {
        Function procedure = convertToFunction(option.getData());

        StringBuilder sb = new StringBuilder();
        sb.append("create procedure ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, procedure.getName()));
        // start params
        if (CollectionUtils.isNotEmpty(procedure.getParams())) {
            sb.append("(");
            for (int i = 0; i < procedure.getParams().size(); i++) {
                if (i != 0) {
                    sb.append(",");
                }
                Param param = procedure.getParams().get(i);
                sb.append(parseParam(param));
            }
            sb.append(")");
        }

        sb.append("\nas").append("\n");
        sb.append(procedure.getSql());
        sb.append(";");

        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDropProcedure(CmdTemplateOption option) {
        return Collections.singletonList("drop procedure " + fmtName(option, option.getSchema()) + "." + fmtName(option, option.getTargetName()) + ";");
    }

    @Override
    public List<String> getAlterView(CmdTemplateOption option) {
        View view = convertToView(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("create or replace ");
        if (parseBoolean(option, OracleAttributeNames.FORCE_CREATE_VIEW)) {
            sb.append("force ");
        }
        sb.append("view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, view.getName())).append("\n");
        sb.append("as");
        sb.append("\n");
        sb.append(view.getSql());
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
    public List<String> getDropTablePrimaryKey(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter table ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" drop constraint ");
        sb.append(fmtName(option, option.getTargetExactName()));
        sb.append(";");
        return Collections.singletonList(sb.toString());
    }

    public List<String> getCompileFunction(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter function ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" compile;");
        return Collections.singletonList(sb.toString());
    }

    public List<String> getCompileProcedure(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter procedure ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" compile;");
        return Collections.singletonList(sb.toString());
    }

    public List<String> getCompileTrigger(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter trigger ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" compile;");
        return Collections.singletonList(sb.toString());
    }

    public List<String> getCompileView(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("alter view ");
        sb.append(fmtName(option, option.getSchema())).append(".").append(fmtName(option, option.getTargetName()));
        sb.append(" compile;");
        return Collections.singletonList(sb.toString());
    }

    private String parseParam(Param param) {
        StringBuilder sb = new StringBuilder();
        sb.append(param.getName()).append(" ");
        if (StringUtils.isNotBlank(param.getMode())) {
            sb.append(param.getMode()).append(" ");
        }
        sb.append(param.getParamType().toLowerCase());
        if (StringUtils.isNotBlank(param.getMode()) && "IN".equalsIgnoreCase(param.getMode()) && param.getDefaultValue() != null) {
            sb.append(" default ").append("'").append(param.getDefaultValue()).append("'");
        }
        return sb.toString();
    }

    @Override
    public List<String> getDbLinkCreate(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        DbLink dbLink = convertToDbLink(option.getData());
        sb.append("create database link ");
        sb.append(fmtName(option, dbLink.getDbLinkName())).append("\n");
        sb.append("connect to ").append(dbLink.getUsername()).append("\n");
        sb.append("identified by \"").append(dbLink.getPassword()).append("\"\n");
        sb.append("using '").append(dbLink.getUrl()).append("'");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getDbLinkDrop(CmdTemplateOption option) {
        String str = "drop database link " + fmtName(option, option.getTargetName());
        return Collections.singletonList(str);
    }

    @Override
    public List<String> getDbLinkTest(CmdTemplateOption option) {
        StringBuilder sb = new StringBuilder();
        sb.append("select 1 from dual @");
        sb.append(fmtName(option, option.getTargetName()));
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getJobCreate(CmdTemplateOption option) {
        Job job = convertToJob(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("declare\n");;
        sb.append("  job number;\n");
        sb.append("    begin\n");
        sb.append("      DBMS_JOB.SUBMIT(\n");
        sb.append("        JOB => job\n");
        sb.append("        ,WHAT => '").append(job.getWhat());
        if (!job.getWhat().trim().endsWith(";")) {
            sb.append(";");
        }
        sb.append("'\n");
        if (job.nextDate.equalsIgnoreCase("sysdate")) {
            sb.append("        ,NEXT_DATE => sysdate\n");
        } else {
            sb.append("        ,NEXT_DATE => to_date('").append(job.getNextDate()).append("', 'yyyy-mm-dd hh24:mi:ss')\n");
        }

        sb.append("        ,INTERVAL => '").append(job.getInterval()).append("'\n");
        sb.append("      );\n");
        sb.append("    commit;\n");
        sb.append("end;");
        return Collections.singletonList(sb.toString());
    }

    @Getter
    @Setter
    protected static class Job {

        private String name;
        private String what;
        private String nextDate;
        private String interval;
    }

    protected Job convertToJob(Map<String, Object> data) {
        Job job = new Job();
        job.setName(parseString(data.get(JobEditorFields.NAME)));
        job.setWhat(parseString(data.get(JobEditorFields.EXEC_SQL)));
        String nextDate = parseString(data.get(OraJobFields.JOB_NEXT_EXEC));
        if (StringUtils.isEmpty(nextDate)) {
            job.setNextDate("sysdate");
        } else {
            job.setNextDate(nextDate);
        }

        String interval = parseString(data.get(JobEditorFields.INTERVAL));
        if (StringUtils.isEmpty(interval)) {
            job.setInterval("null");
        } else {
            job.setInterval(interval);
        }
        return job;
    }

    public List<String> getJobAlter(CmdTemplateOption option) {
        Job job = convertToJob(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("declare\n");;
        sb.append("  job number;\n");
        sb.append("    begin\n");
        sb.append("      DBMS_JOB.CHANGE(\n");
        sb.append("        JOB => ").append(option.getTargetName()).append("\n");
        sb.append("        ,WHAT => '").append(job.getWhat());
        if (!job.getWhat().trim().endsWith(";")) {
            sb.append(";");
        }
        sb.append("'\n");
        if (job.nextDate.equalsIgnoreCase("sysdate")) {
            sb.append("        ,NEXT_DATE => sysdate\n");
        } else {
            sb.append("        ,NEXT_DATE => to_date('").append(job.getNextDate()).append("', 'yyyy-mm-dd hh24:mi:ss')\n");
        }
        sb.append("        ,INTERVAL => '").append(job.getInterval()).append("'\n");
        sb.append("      );\n");
        sb.append("    commit;\n");
        sb.append("end;");
        return Collections.singletonList(sb.toString());
    }

    @Override
    public List<String> getJobDrop(CmdTemplateOption option) {
        String sql = "begin\n  dbms_job.remove(%s);\n  commit;\nend;";

        return Collections.singletonList(String.format(sql, option.getTargetName()));
    }

    @Override
    public List<String> getJobStop(CmdTemplateOption option) {
        String sql = "begin\n    dbms_job.broken (%s, TRUE); \ncommit;\nend;";
        return Collections.singletonList(String.format(sql, option.getTargetName()));
    }

    @Override
    public List<String> getJobRun(CmdTemplateOption option) {
        String sql = "begin\n    dbms_job.run(%s);\nend;";
        return Collections.singletonList(String.format(sql, option.getTargetName()));
    }

    public List<String> getJobResume(CmdTemplateOption option) {
        String sql = "begin\n" + "  dbms_job.broken(%s, FALSE);\n" + "  commit;\n" + "end;";
        return Collections.singletonList(String.format(sql, option.getTargetName()));
    }

    public List<String> getScheduleJobDrop(CmdTemplateOption option) {
        String sql = "begin\n  dbms_scheduler.drop_job(\n    job_name => '%s'\n  );\nend;";
        String format = String.format(sql, option.getTargetName());
        return Collections.singletonList(format);
    }

    public List<String> getScheduleJobDisable(CmdTemplateOption option) {
        String sql = "begin\n  dbms_scheduler.disable(\n    name => '%s'\n  );\nend;";
        String format = String.format(sql, option.getTargetName());
        return Collections.singletonList(format);
    }

    public List<String> getScheduleJobEnable(CmdTemplateOption option) {
        String sql = "begin\n  dbms_scheduler.enable(\n    name => '%s'\n  );\nend;";
        String format = String.format(sql, option.getTargetName());
        return Collections.singletonList(format);
    }

    public List<String> getScheduleJobRun(CmdTemplateOption option) {
        String sql = "begin\n  dbms_scheduler.run_job(job_name => '%s', use_current_session => FALSE);\nend;";
        String format = String.format(sql, option.getTargetName());
        return Collections.singletonList(format);
    }

    public List<String> getScheduleJobCreate(CmdTemplateOption option) {
        ScheduleJob scheduleJob = new ScheduleJob(option.getData());
        StringBuilder sb = new StringBuilder();
        sb.append("begin\n");
        sb.append("  dbms_scheduler.create_job(\n");
        sb.append("    job_name => '").append(option.getSchema()).append(".").append(scheduleJob.getJobName()).append("'\n");
        sb.append("    ,job_type => 'PLSQL_BLOCK'\n");
        sb.append("    ,job_action => '").append(scheduleJob.getJobAction()).append("'\n");
        if (StringUtils.isNotEmpty(scheduleJob.getStartDate())) {
            sb.append("    ,start_date => to_date('").append(scheduleJob.getStartDate()).append("', 'yyyy-mm-dd hh24:mi:ss')\n");
        }
        if (StringUtils.isNotEmpty(scheduleJob.getEndDate())) {
            sb.append("    ,end_date => to_date('").append(scheduleJob.getEndDate()).append("', 'yyyy-mm-dd hh24:mi:ss')\n");
        }
        if (StringUtils.isNotEmpty(scheduleJob.getRepeatInterval())) {
            sb.append("    ,repeat_interval => '").append(scheduleJob.getRepeatInterval()).append("'\n");
        }
        sb.append("    ,enabled => ").append(scheduleJob.getEnabled() != null && scheduleJob.getEnabled()).append("\n");
        sb.append("    ,auto_drop => ").append(scheduleJob.getAutoDrop() != null && scheduleJob.getAutoDrop()).append("\n");
        if (StringUtils.isNotEmpty(scheduleJob.getComments())) {
            sb.append("    ,comments => '").append(scheduleJob.getComments()).append("'\n");
        }
        sb.append("  );\n");
        sb.append("end;");
        return Collections.singletonList(sb.toString());
    }

    @Getter
    @Setter
    protected static class ScheduleJob {

        private String  jobName;
        private String  jobType;
        private String  jobAction;
        private String  startDate;
        private String  repeatInterval;
        private String  endDate;
        private String  jobClass;
        private Boolean enabled;
        private Boolean autoDrop;
        private String  comments;

        protected ScheduleJob(Map<String, Object> data){
            this.jobName = (String) data.get(OraScheduleJobFields.NAME);
            this.jobType = (String) data.get(OraScheduleJobFields.JOB_TYPE);
            this.jobAction = (String) data.get(OraScheduleJobFields.JOB_ACTION);
            this.startDate = (String) data.get(OraScheduleJobFields.START_DATE);
            this.repeatInterval = (String) data.get(OraScheduleJobFields.REPEAT_INTERVAL);
            this.endDate = (String) data.get(OraScheduleJobFields.END_DATE);
            //            this.jobClass = (String) data.get(ScheduleJobFields.JOB_CLASS);
            this.enabled = (Boolean) data.get(OraScheduleJobFields.ENABLED);
            this.autoDrop = (Boolean) data.get(OraScheduleJobFields.AUTO_DROP);
            this.comments = (String) data.get(OraScheduleJobFields.COMMENTS);
        }
    }

    @Getter
    @Setter
    protected static class DbLink {

        private String dbLinkName;
        private String username;
        private String password;
        private String url;
    }

    protected DbLink convertToDbLink(Map<String, Object> data) {
        DbLink dbLink = new DbLink();
        dbLink.setDbLinkName(parseString(data.get(OraDbLinkFields.DBLINK_NAME)));
        dbLink.setUsername(parseString(data.get(OraDbLinkFields.LINK_USERNAME)));
        dbLink.setPassword(parseString(data.get(OraDbLinkFields.LINK_PASSWORD)));
        dbLink.setUrl(parseString(data.get(OraDbLinkFields.LINK_URL)));
        return dbLink;
    }

    @Override
    protected Dialect getDialect() { return OracleDialect.INSTANCE; }
}
