package com.clougence.clouddm.ds.oracle.definition.ui.editor.job;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.job.JobEditorFields;

public interface OraJobFields extends JobEditorFields {

    //    String JOB_WHAT      = OracleAttributeNames.JOB_WHAT.getCodeKey();
    String JOB_LAST_EXEC = OracleAttributeNames.JOB_LAST_EXEC.getCodeKey();
    String JOB_NEXT_EXEC = OracleAttributeNames.JOB_NEXT_EXEC.getCodeKey();
}
