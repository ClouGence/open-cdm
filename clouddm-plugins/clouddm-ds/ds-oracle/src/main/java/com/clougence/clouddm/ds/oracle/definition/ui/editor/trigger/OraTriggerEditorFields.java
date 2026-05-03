package com.clougence.clouddm.ds.oracle.definition.ui.editor.trigger;

import com.clougence.adapter.oracle.OracleAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public interface OraTriggerEditorFields extends TriggerEditorFields {

    String NEW_ALIAS           = OracleAttributeNames.NEW_ALIAS.getCodeKey();
    String OLD_ALIAS           = OracleAttributeNames.OLD_ALIAS.getCodeKey();
    String TRIGGER_GRANULARITY = OracleAttributeNames.TRIGGER_GRANULARITY.getCodeKey();
    String CONDITION           = OracleAttributeNames.CONDITION.getCodeKey();

    String OBJ_ENABLED         = OracleAttributeNames.OBJ_ENABLED.getCodeKey();
    String CREATE_TIME         = OracleAttributeNames.CREATE_TIME.getCodeKey();
    String LAST_DDL_TIME       = OracleAttributeNames.LAST_DDL_TIME.getCodeKey();
    String OBJ_STATUS          = OracleAttributeNames.OBJ_STATUS.getCodeKey();
}
