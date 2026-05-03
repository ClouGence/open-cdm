package com.clougence.clouddm.ds.dameng.definition.ui.editor.trigger;

import com.clougence.adapter.dameng.DmAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public interface DmTriggerEditorFields extends TriggerEditorFields {

    String NEW_ALIAS           = DmAttributeNames.NEW_ALIAS.getCodeKey();
    String OLD_ALIAS           = DmAttributeNames.OLD_ALIAS.getCodeKey();
    String TRIGGER_GRANULARITY = DmAttributeNames.TRIGGER_GRANULARITY.getCodeKey();
    String CONDITION           = DmAttributeNames.CONDITION.getCodeKey();
    String ENCRYPTION          = DmAttributeNames.ENCRYPTION.getCodeKey();
}
