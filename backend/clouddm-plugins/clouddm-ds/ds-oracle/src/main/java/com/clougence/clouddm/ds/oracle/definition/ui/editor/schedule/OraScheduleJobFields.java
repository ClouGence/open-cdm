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
