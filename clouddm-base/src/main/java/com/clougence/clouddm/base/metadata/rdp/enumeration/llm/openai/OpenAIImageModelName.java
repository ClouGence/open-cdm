package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai;

public enum OpenAIImageModelName {

    DALL_E_2("dall-e-2"),
    DALL_E_3("dall-e-3");

    private final String stringValue;

    OpenAIImageModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
