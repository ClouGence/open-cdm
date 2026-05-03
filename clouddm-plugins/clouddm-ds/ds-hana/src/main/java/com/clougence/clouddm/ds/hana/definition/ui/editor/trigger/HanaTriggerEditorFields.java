package com.clougence.clouddm.ds.hana.definition.ui.editor.trigger;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public interface HanaTriggerEditorFields extends TriggerEditorFields {

    String NEW_ALIAS           = HanaAttributeNames.NEW_ALIAS.getCodeKey();
    String OLD_ALIAS           = HanaAttributeNames.OLD_ALIAS.getCodeKey();
    String TRIGGER_GRANULARITY = HanaAttributeNames.TRIGGER_GRANULARITY.getCodeKey();
    String CONDITION           = HanaAttributeNames.CONDITION.getCodeKey();
}
