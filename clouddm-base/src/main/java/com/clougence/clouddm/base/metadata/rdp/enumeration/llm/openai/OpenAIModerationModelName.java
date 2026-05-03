package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai;

public enum OpenAIModerationModelName {

    TEXT_MODERATION_STABLE("text-moderation-stable"),
    TEXT_MODERATION_LATEST("text-moderation-latest");

    private final String stringValue;

    OpenAIModerationModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
