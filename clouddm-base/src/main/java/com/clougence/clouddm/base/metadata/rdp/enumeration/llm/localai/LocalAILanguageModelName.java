package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.localai;

public enum LocalAILanguageModelName {

    GPT_4("gpt-4");

    private final String stringValue;

    LocalAILanguageModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
