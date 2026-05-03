package com.clougence.clouddm.base.metadata.ui.form.value;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
public final class MapValueDef implements ValueDef {

    private Map<String, Object> data;

    @Tolerate
    public MapValueDef(){
    }

    @Override
    public Object asValue() {
        return this.data;
    }
}
