package com.clougence.clouddm.sdk.ui.editor.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author caishan
 * @date 2023-10-25 11:04:51
 * @version: 1.0
 */

@Getter
@Setter
public class DataEditorHeader {

    private String type;
    private String expr;
    private String message;

    public DataEditorHeader(){
    }

    public DataEditorHeader(String type, String expr, String message){
        this.type = type;
        this.expr = expr;
        this.message = message;
    }
}
