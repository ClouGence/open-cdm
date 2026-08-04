package com.clougence.clouddm.ds;

public abstract class TextTestCase {

    private final String resourcePath;
    private final String name;
    private final int    index;

    protected TextTestCase(TextCaseSupport.CaseBlock block){
        this(block.resourcePath(), block.name(), block.index());
    }

    protected TextTestCase(String resourcePath, String name, int index){
        this.resourcePath = resourcePath;
        this.name = name;
        this.index = index;
    }

    public final String resourcePath() {
        return resourcePath;
    }

    public final String name() {
        return name;
    }

    public final int index() {
        return index;
    }

    public final String caseId() {
        return resourcePath + "#" + name;
    }

    public final String caseIndexId() {
        return resourcePath + "#" + String.format("%03d", index);
    }
}
