package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.openai;

public enum OpenAILanguageModelName {

    GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct");

    private final String stringValue;

    OpenAILanguageModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
