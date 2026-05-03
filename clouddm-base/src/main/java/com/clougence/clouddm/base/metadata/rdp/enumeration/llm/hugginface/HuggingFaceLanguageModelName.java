package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.hugginface;

public enum HuggingFaceLanguageModelName {

    TII_UAE_FALCON_7B_INSTRUCT("tiiuae/falcon-7b-instruct");

    private final String stringValue;

    HuggingFaceLanguageModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
