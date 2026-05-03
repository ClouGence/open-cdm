package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.sequence;

import com.clougence.adapter.db2.Db2AttributeNames;
import com.clougence.clouddm.sdk.ui.editor.sequence.SequenceFields;

public interface Db2SequenceFields extends SequenceFields {

    String CREATE_TIME = Db2AttributeNames.CREATE_DATE.getCodeKey();
    String UPDATE_TIME = Db2AttributeNames.UPDATE_DATE.getCodeKey();

    String CYCLE_FLAG  = Db2AttributeNames.CYCLE.getCodeKey();
    String CACHE_SIZE  = Db2AttributeNames.CACHE_SIZE.getCodeKey();
    String LAST_NUMBER = Db2AttributeNames.NEXT_CACHE_VALUE.getCodeKey();
    String ORDER       = Db2AttributeNames.ORDER.getCodeKey();
    String START_VALUE = Db2AttributeNames.START_VALUE.getCodeKey();

}
