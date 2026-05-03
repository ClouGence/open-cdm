package com.clougence.clouddm.base.metadata.ui.form.value;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface ValueDef {

    Object asValue();
}
