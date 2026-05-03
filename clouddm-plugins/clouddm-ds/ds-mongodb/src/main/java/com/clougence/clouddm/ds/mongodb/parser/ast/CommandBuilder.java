package com.clougence.clouddm.ds.mongodb.parser.ast;

import java.util.LinkedHashMap;

public class CommandBuilder extends LinkedHashMap<String, String> {

    public String toString() {
        StringBuilder command = new StringBuilder();
        command.append("{");
        this.forEach((k, v) -> {
            command.append("\"").append(k).append("\":").append(v).append(",");
        });
        command.deleteCharAt(command.length() - 1);
        command.append("}");
        return command.toString();
    }

    public void addOption(String option, String value) {
        if (value == null) {
            return;
        }
        this.put(option, value);
    }
}
