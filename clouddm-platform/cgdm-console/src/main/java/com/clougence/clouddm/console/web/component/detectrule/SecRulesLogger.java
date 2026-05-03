package com.clougence.clouddm.console.web.component.detectrule;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecRulesLogger {

    private String ruleName;
    private String logMessage;

    public SecRulesLogger(String ruleName, String logMessage){
        this.ruleName = ruleName;
        this.logMessage = logMessage;
    }
}
