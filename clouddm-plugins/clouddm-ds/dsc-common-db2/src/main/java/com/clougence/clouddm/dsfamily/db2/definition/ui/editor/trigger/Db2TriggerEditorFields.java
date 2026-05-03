package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.trigger;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.trigger.TriggerEditorFields;

public interface Db2TriggerEditorFields extends TriggerEditorFields {

    String NEW_ALIAS           = Db2AttributeNames.NEW_ALIAS.getCodeKey();
    String OLD_ALIAS           = Db2AttributeNames.OLD_ALIAS.getCodeKey();
    String NEW_TABLE_ALIAS     = Db2AttributeNames.NEW_TABLE_ALIAS.getCodeKey();
    String OLD_TABLE_ALIAS     = Db2AttributeNames.OLD_TABLE_ALIAS.getCodeKey();
    String TRIGGER_GRANULARITY = Db2AttributeNames.TRIGGER_GRANULARITY.getCodeKey();
    String CONDITION           = Db2AttributeNames.CONDITION.getCodeKey();

    String CREATE_TIME         = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME         = Db2AttributeNames.UPDATE_DATE.getCodeKey();
    String DELETE_EVENT        = Db2AttributeNames.DELETE_EVENT.getCodeKey();
    String INSERT_EVENT        = Db2AttributeNames.INSERT_EVENT.getCodeKey();
    String UPDATE_EVENT        = Db2AttributeNames.UPDATE_EVENT.getCodeKey();
    String TRIGGER_SECURE      = Db2AttributeNames.SECURE.getCodeKey();
}
