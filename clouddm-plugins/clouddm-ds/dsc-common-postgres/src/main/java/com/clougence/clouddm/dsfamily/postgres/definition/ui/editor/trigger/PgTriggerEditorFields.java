package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.trigger;

import com.clougence.adapter.postgre.PostgreAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public interface PgTriggerEditorFields extends TriggerEditorFields {

    String TRIGGER_CONSTRAINT            = PostgreAttributeNames.TRIGGER_CONSTRAINT.getCodeKey();
    String TRIGGER_REFERENCED_TABLE_NAME = PostgreAttributeNames.TRIGGER_REFERENCED_TABLE_NAME.getCodeKey();
    String TRIGGER_TIMING                = PostgreAttributeNames.TRIGGER_TIMING.getCodeKey();
    String NEW_ALIAS                     = PostgreAttributeNames.NEW_ALIAS.getCodeKey();
    String OLD_ALIAS                     = PostgreAttributeNames.OLD_ALIAS.getCodeKey();
    String TRIGGER_GRANULARITY           = PostgreAttributeNames.TRIGGER_GRANULARITY.getCodeKey();
    String TRIGGER_CONDITION             = PostgreAttributeNames.TRIGGER_CONDITION.getCodeKey();
}
