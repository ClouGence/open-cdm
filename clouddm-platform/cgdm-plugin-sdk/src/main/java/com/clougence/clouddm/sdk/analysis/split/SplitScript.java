package com.clougence.clouddm.sdk.analysis.split;

import java.util.List;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SplitScript {

    private SecQueryType   type;
    private String         script;
    private List<QueryArg> scriptArgs;

    private int            bodyStartCodeLine;
    private int            bodyStartCodeColumn;
    private int            bodyEndCodeLine;
    private int            bodyEndCodeColumn;

    public SplitScript(){
    }

    public SplitScript(SecQueryType type, String script, List<QueryArg> scriptArgs){
        this.type = type;
        this.script = script;
        this.scriptArgs = scriptArgs;
    }
}
