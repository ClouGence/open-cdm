package com.clougence.schema.editor;

import com.clougence.schema.umi.struts.AttributeUmiData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditorOptions extends AttributeUmiData {

    private boolean useDelimited = false;
    private boolean skipHandlers = false;
}
