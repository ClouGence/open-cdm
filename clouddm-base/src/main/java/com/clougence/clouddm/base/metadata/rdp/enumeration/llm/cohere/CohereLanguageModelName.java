package com.clougence.clouddm.base.metadata.rdp.enumeration.llm.cohere;

public enum CohereLanguageModelName {

    COMMAND("command"),
    COMMAND_NIGHTLY("command-nightly"),
    COMMAND_LIGHT("command-light"),
    COMMAND_LIGHT_NIGHTLY("command-light-nightly");

    private final String stringValue;

    CohereLanguageModelName(String stringValue){
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
