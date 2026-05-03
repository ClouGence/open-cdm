package com.clougence.dslparser.detectrule.test.rule;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RuleDomain {

    private SecQueryType        sqlType;
    private SqlDdlKind          ddlKind;
    private Map<String, String> options;

    public TargetType getSqlTarget() { return this.sqlType.getTarget(); }

    public SecDataAuthKind getSqlKind() { return this.sqlType.getKind(); }
}
