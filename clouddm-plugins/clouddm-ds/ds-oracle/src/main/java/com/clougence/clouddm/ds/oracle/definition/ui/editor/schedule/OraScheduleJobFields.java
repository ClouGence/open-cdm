package com.clougence.clouddm.ds.oracle.definition.ui.editor.schedule;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.schedule.ScheduleJobFields;

public interface OraScheduleJobFields extends ScheduleJobFields {

    //    String NAME            = "name";
    //    String JOB_TYPE        = "jobType";
    //    String JOB_ACTION      = "jobAction";
    //    String JOB_CLASS       = "jobClass";
    //    String ENABLED         = "enabled";
    //    String COMMENTS        = "comments";
    String AUTO_DROP       = OracleAttributeNames.SCHEDULE_JOB_AUTO_DROP.getCodeKey();
    String START_DATE      = OracleAttributeNames.SCHEDULE_JOB_START_DATE.getCodeKey();
    String END_DATE        = OracleAttributeNames.SCHEDULE_JOB_END_DATE.getCodeKey();
    String REPEAT_INTERVAL = OracleAttributeNames.SCHEDULE_JOB_REPEAT_INTERVAL.getCodeKey();
    String JOB_TYPE        = OracleAttributeNames.SCHEDULE_JOB_TYPE.getCodeKey();

    //    String BASE_INFO       = "baseInfo";

}
