package com.clougence.dslparser.detectrule.test.rule;

import com.clougence.detectrule.lang.EnumAccess;
import com.clougence.utils.StringUtils;

import lombok.Getter;

@Getter
public enum SqlDdlKind implements EnumAccess<SqlDdlKind> {

    Create("c"),
    Alter("a"),
    Drop("d"),
    Rename("r");

    private final String code;

    SqlDdlKind(String code){
        this.code = code;
    }

    @Override
    public String codeName() {
        return this.code;
    }

    @Override
    public SqlDdlKind valueOfCode(String codeString) {
        if (StringUtils.equalsIgnoreCase(codeString, "c")) {
            return Create;
        } else if (StringUtils.equalsIgnoreCase(codeString, "a")) {
            return Alter;
        } else if (StringUtils.equalsIgnoreCase(codeString, "d")) {
            return Drop;
        } else if (StringUtils.equalsIgnoreCase(codeString, "r")) {
            return Rename;
        } else {
            return null;
        }
    }
}
