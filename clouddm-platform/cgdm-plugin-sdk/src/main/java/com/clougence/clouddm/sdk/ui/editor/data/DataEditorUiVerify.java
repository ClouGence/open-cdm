package com.clougence.clouddm.sdk.ui.editor.data;

/**
 * @author caishan
 * @date 2023-10-25 10:18:54
 * @version: 1.0
 * @description data format verification type
 */
public enum DataEditorUiVerify {

    DATA_FORMAT_HEXADECIMAL("dataFormat", "^(0x|0X)?[0-9A-Fa-f]+$"),
    DATA_FORMAT_BINARY("dataFormat", "^[01]+$"),
    DATA_FORMAT_BINARY_OR_HEX("dataFormat", "^[01]+$|^(0x|0X)?[0-9A-Fa-f]+$");

    private String type; //parity bit
    private String expr; //regular expression

    DataEditorUiVerify(String type, String expr){
        this.type = type;
        this.expr = expr;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getExpr() { return expr; }

    public void setExpr(String expr) { this.expr = expr; }
}
