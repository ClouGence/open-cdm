package com.clougence.clouddm.sec.rules.domain.func;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncGroup {

    public static final FuncGroup INSTANCE   = new FuncGroup();

    private FuncNumberUtils       number     = new FuncNumberUtils();
    private FuncStringUtils       string     = new FuncStringUtils();
    private FuncArrayUtils        array      = new FuncArrayUtils();
    private FuncLoggerUtils       log        = new FuncLoggerUtils();

    private FuncConstraintUtils   constraint = FuncConstraintUtils.INSTANCE;
}
