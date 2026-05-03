package com.clougence.clouddm.base.metadata.ui.form.value;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
public final class ObjectValueDef implements ValueDef {

    private Object value;

    @Tolerate
    public ObjectValueDef(){
    }

    @Override
    public Object asValue() {
        return this.value;
    }
}
