package com.clougence.clouddm.base.metadata.ui.form;

public enum UiPanelFieldType {

    // standard
    Input("Input"),
    Options("Options"),
    Radios("Radios"),
    Check("Check"),
    Tags("Tag"),
    TextArea("TextArea"),
    MultipleOptions("MultipleOptions"),
    CheckBox("CheckBox"),
    SelectorList("SelectorList"),
    Fold("Fold"),

    // special
    CheckboxColumns("CheckboxColumns"),
    SelectCheckedColumns("SelectCheckedColumns"),
    Columns("Columns"),
    SelectColumns("SelectColumns"),
    JsonColumns("JsonColumns"),
    Tree("Tree"),
    Map("Map"),
    MapItem("MapItem"),
    // reference
    ReferenceRelation("ReferenceRelation"),
    ReferenceSchema("ReferenceSchema"),
    ReferenceTable("ReferenceTable"),
    ReferenceColumn("ReferenceColumn"),

    //
    TriggerColumns("TriggerColumns"),

    // 
    PartitionDefineList("PartitionDefineList"),
    PartitionTemplateList("PartitionTemplateList"),

    Date("Date"),
    Time("Time"),
    DateTime("DateTime");

    private final String type;

    UiPanelFieldType(String type){
        this.type = type;
    }

    public String getType() { return this.type; }
}
